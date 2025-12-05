package io.switstack.switcloud.switcloud_l2_demo.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import io.switstack.switcloud.switcloud_l2_demo.data.TlvEntry
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvConfig
import io.switstack.switcloud.switcloud_l2_demo.utils.MokaConfig
import io.switstack.switcloud.switcloud_l2_demo.utils.Utils
import io.switstack.switcloud.switcloudl2.SwitcloudL2
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2Exception
import io.switstack.switcloud.switcloudl2.exception.SwitcloudL2NotFoundException
import io.switstack.switcloud.switcloudl2.helpers.CardInterfaceType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PaymentViewModel() : ViewModel() {

    fun processPayment(activity: Activity, amount: String): String {

        // Construct the 'trd' byte array in TLV format
        val trd = createTrd(amount)

        // Pseudocode for the logic (replace with your actual classes and methods)
        val switcloudL2 = SwitcloudL2.getInstance()
        switcloudL2.setActivity(activity)
        switcloudL2.initializeServices()

        val glase = switcloudL2.glase()
        val reader = switcloudL2.reader()

        reader.configure(MokaConfig.readerParams)

        glase.configureEntryPoint(EmvConfig.entryPointConfiguration)
        glase.addCombination(EmvConfig.combinationMastercard)
        glase.addCombination(EmvConfig.combinationVisa)
        glase.addCAKey(EmvConfig.caKey)

        val preProcessingResult = glase.preProcessing(trd)
        if (!preProcessingResult.second)
            throw SwitcloudL2Exception("Pre-processing failed")

        val card = glase.protocolActivation(null)
        if (card != CardInterfaceType.CARD_INTERFACE_TYPE_CONTACTLESS)
            throw SwitcloudL2Exception("Card detection error")

        val combinationSelectionResult = glase.combinationSelection()
        if (!combinationSelectionResult.second)
            throw SwitcloudL2Exception("Combination selection failed")

        glase.kernelActivation(null)

        switcloudL2.cleanupServices()

        // Items to show on ticket
        val ticketTags = listOf(
            "9C",    // TT
            "9A",    // Data
            "9F02",  // Amount
            "4F",    // AID
            "84",    // DF Name
            "50",    // Application label
            "5A",    // PAN
            "9F27",  // CID
            "95",    // TVR
            "DF8129" // OPS
        )

        var ticketData: ByteArray = byteArrayOf()
        for (tag in ticketTags) {
            try {
                glase.getTag(Utils.hexStringToByteArray(tag))?.let { ticketData += it }
            } catch (e: SwitcloudL2NotFoundException) {
                // Skip that tag
            }
        }

        return Utils.byteArrayToHexString(ticketData)
    }

    fun parseTlvString(tlvString: String?): List<TlvEntry> {
        if(tlvString == null) return emptyList()

        val tlvBytes = Utils.hexStringToByteArray(tlvString)
        val tlvEntries = mutableListOf<TlvEntry>()
        var offset = 0

        while (offset < tlvBytes.size) {
            // Parse Tag
            var tagBytes = 1
            var tag = tlvBytes[offset].toInt() and 0xFF
            if ((tag and 0x1F) == 0x1F) { // Multi-byte tag
                tagBytes++
                while ((tlvBytes[offset + tagBytes - 1].toInt() and 0x80) == 0x80) {
                    tagBytes++
                }
            }
            val tagHex = Utils.byteArrayToHexString(tlvBytes.copyOfRange(offset, offset + tagBytes))
            offset += tagBytes

            // Parse Length
            var lengthBytes = 1
            var length = tlvBytes[offset].toInt() and 0xFF
            if ((length and 0x80) == 0x80) { // Multi-byte length
                val numLengthBytes = length and 0x7F
                length = 0
                for (i in 0 until numLengthBytes) {
                    length = (length shl 8) or (tlvBytes[offset + 1 + i].toInt() and 0xFF)
                }
                lengthBytes += numLengthBytes
            }
            offset += lengthBytes

            // Parse Value
            val valueBytes = tlvBytes.copyOfRange(offset, offset + length)
            val valueHex = Utils.byteArrayToHexString(valueBytes)
            tlvEntries.add(TlvEntry(tagHex, valueHex))
            offset += length
        }
        return tlvEntries
    }

    // Helper function to convert an integer to a BCD byte array of a specific length
    private fun intToBcd(value: Int, length: Int): ByteArray {
        val bcd = ByteArray(length)
        var temp = value
        for (i in length - 1 downTo 0) {
            bcd[i] = ((temp % 10) or ((temp / 10 % 10) shl 4)).toByte()
            temp /= 100
        }
        return bcd
    }

    private fun createTrd(amount: String): ByteArray {
        return buildList<Byte> {
            // 1. Transaction Type (tag: 9c, length: 01, value: 00)
            add(0x9C.toByte()) // Tag 9C
            add(0x01.toByte()) // Length 01
            add(0x00.toByte()) // Value 00

            // 2. Date (tag: 9a, length: 03, value: current date in BCD YY MM DD)
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyMMdd", Locale.US)
            val dateString = dateFormat.format(calendar.time)
            val dateBcd = Utils.hexStringToByteArray(dateString)

            add(0x9A.toByte()) // Tag 9A
            add(0x03.toByte()) // Length 03
            addAll(dateBcd.toList())

            // 3. Amount (tag: 9f02, length: 06, value: convert amount string to BCD)
            val parsedAmount = amount.replace(".", "").toIntOrNull() ?: 0
            val amountBcd = intToBcd(parsedAmount, 6) // Amount in cents, 6 bytes BCD

            add(0x9F.toByte()) // Tag 9F02 (first byte)
            add(0x02.toByte()) // Tag 9F02 (second byte)
            add(0x06.toByte()) // Length 06
            addAll(amountBcd.toList())
        }.toByteArray()
    }

}