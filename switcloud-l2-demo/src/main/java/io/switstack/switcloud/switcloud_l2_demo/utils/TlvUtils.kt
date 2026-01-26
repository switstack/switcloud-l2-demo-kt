package io.switstack.switcloud.switcloud_l2_demo.utils

import com.payneteasy.tlv.BerTag
import com.payneteasy.tlv.BerTlvBuilder
import com.payneteasy.tlv.BerTlvParser
import com.payneteasy.tlv.HexUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.switstack.switcloud.switcloud_l2_demo.SwitcloudL2DemoException
import io.switstack.switcloud.switcloud_l2_demo.data.TlvEntry
import io.switstack.switcloud.switcloudapi.model.CAPKCreateSchema
import io.switstack.switcloud.switcloudapi.model.EMVCreateSchema
import io.switstack.switcloud.switcloudapi.model.EMVTransactionType
import okio.IOException
import org.openapitools.client.infrastructure.UUIDAdapter

object TlvUtils {

    val moshi by lazy {
        Moshi.Builder()
            .add(UUIDAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    fun parseTlvString(tlvString: String?): List<TlvEntry> {
        if (tlvString == null) return emptyList()

        val tlvBytes = ByteArrayHexStringUtils.hexStringToByteArray(tlvString)
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
            val tagHex = ByteArrayHexStringUtils.byteArrayToHexString(
                tlvBytes.copyOfRange(
                    offset,
                    offset + tagBytes
                )
            )
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
            val valueHex = ByteArrayHexStringUtils.byteArrayToHexString(valueBytes)
            tlvEntries.add(TlvEntry(tagHex, valueHex))
            offset += length
        }
        return tlvEntries
    }

    fun makeGlaseEntryPointConfiguration(trd: ByteArray): ByteArray {
        val parser = BerTlvParser()
        val tlvs = parser.parse(trd) ?: throw SwitcloudL2DemoException("ill-formed TLV")

        val builder = BerTlvBuilder()
        tlvs.list.forEach {
            if (it.tag == BerTag(0x9C)) {
                builder.addHex(BerTag(0xDF, 0xA0, 0x25), it.hexValue)
            }

            // do not process other tags for now
        }

        return builder.buildArray()
    }

    fun <T> parseJsonToScheme(jsonString: String, modelClass: Class<T>): T? {
        try {
            val adapter = moshi.adapter(modelClass)
            return adapter.fromJson(jsonString)
        } catch (e: IOException) {
            throw SwitcloudL2DemoException(e)
        }
    }

    fun makeGlaseCombination(emv: EMVCreateSchema): ByteArray {
        val e1Data = BerTlvBuilder()

        // AID
        e1Data.addHex(BerTag(0x9F, 0x06), emv.aid)

        // Kernel ID
        e1Data.addHex(BerTag(0xDF, 0x81, 0x0C), emv.kernel)

        // Transaction type
        when (val transactionType = emv.transactionType) {
            EMVTransactionType._00 ->
                e1Data.addHex(BerTag(0x9C), "00")

            EMVTransactionType._01 ->
                e1Data.addHex(BerTag(0x9C), "01")

            EMVTransactionType._09 ->
                e1Data.addHex(BerTag(0x9C), "09")

            EMVTransactionType._17 ->
                e1Data.addHex(BerTag(0x9C), "17")

            EMVTransactionType._20 ->
                e1Data.addHex(BerTag(0x9C), "20")

            else ->
                throw SwitcloudL2DemoException(
                    "Unsupported Transaction Type: $transactionType"
                )
        }

        // ASF
        e1Data.addHex(
            BerTag(0xDF, 0xA0, 0x10),
            if (emv.asf == null || emv.asf == false) "00" else "01"
        )

        // E2 group
        e1Data.addHex(BerTag(0xE2), emv.tlv)

        val e1 = BerTlvBuilder()
        e1.addBytes(BerTag(0xE1), e1Data.buildArray())

        return e1.buildArray()
    }

    private fun sizeToBEBytes(size: Int, outputByteSize: Int): ByteArray {
        val bytes = ByteArray(outputByteSize)

        for (b in 0..<outputByteSize) {
            bytes[outputByteSize - 1 - b] = (((size shr (b shl 3)) and 0xFF).toByte())
        }

        return bytes
    }

    fun makeGlaseCAKey(capk: CAPKCreateSchema): ByteArray {
        val maxIndexSize = 2
        val maxModulusSize = 512
        val modulusLengthSize = 4
        val maxExponentSize = 6
        val exponentLengthSize = 2
        val maxHashSize = 40
        val hashIndicatorUndefined = "00"
        val algorithmIndicatorRSA = "00"

        val rid = capk.rid
        val index = capk.index
        val modulus = capk.modulus
        val exponent = capk.exponent
        val hash = capk.hash

        // Check sizes against maximum allowed sizes
        if (index.length > maxIndexSize) {
            throw SwitcloudL2DemoException("index too long")
        }
        if (modulus.length > maxModulusSize) {
            throw SwitcloudL2DemoException("modulus too long")
        }
        if (exponent.length > maxExponentSize) {
            throw SwitcloudL2DemoException("public exponent too long")
        }
        if (hash.length > maxHashSize) {
            throw SwitcloudL2DemoException("hash too long")
        }

        val modulusLength = sizeToBEBytes(modulus.length / 2, modulusLengthSize / 2)
        val exponentLength = sizeToBEBytes(exponent.length / 2, exponentLengthSize / 2)

        val cakey = rid +
            index +
            hashIndicatorUndefined +
            algorithmIndicatorRSA +
            HexUtil.toHexString(modulusLength) +
            modulus.padEnd(maxModulusSize, '0') +
            HexUtil.toHexString(exponentLength) +
            exponent.padEnd(maxExponentSize, '0') +
            hash

        val builder = BerTlvBuilder()
        builder.addHex(BerTag(0xDF, 0xA0, 0x23), cakey)

        return builder.buildArray()
    }
}