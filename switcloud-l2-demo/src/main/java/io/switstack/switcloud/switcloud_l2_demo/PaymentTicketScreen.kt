package io.switstack.switcloud.switcloud_l2_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme
import io.switstack.switcloud.switcloud_l2_demo.utils.EmvUtils
import io.switstack.switcloud.switcloud_l2_demo.utils.Utils

data class TlvEntry(val tag: String, val value: String)

fun parseTlvString(tlvString: String): List<TlvEntry> {
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

@Composable
fun PaymentTicketScreen(navController: NavController, success: Boolean, tlvStream: String?) {
    Switcloudl2demoktTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (success) {
                    Text("Payment Successful!")
                    Spacer(modifier = Modifier.height(16.dp))

                    tlvStream?.let { tlvString ->
                        val tlvEntries = parseTlvString(tlvString)
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            tlvEntries.forEach { entry ->
                                val tagName = EmvUtils.emvTagNames[entry.tag.uppercase()] ?: entry.tag
                                val displayValue = if (entry.tag.uppercase() == "9C") {
                                    EmvUtils.transactionTypeToString(entry.value)
                                } else if (EmvUtils.isTagASCII(entry.tag)) {
                                    Utils.hexStringToAsciiString(entry.value)
                                } else {
                                    entry.value.uppercase()
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = tagName)
                                    Text(text = displayValue)
                                }
                            }
                        }
                    }
                } else {
                    Text("Payment Failed")
                }
            }
            Column {
                Action(buttonText = "Back to Cart") {
                    navController.navigate("shopping_cart")
                }
                Footer()
            }
        }
    }
}
