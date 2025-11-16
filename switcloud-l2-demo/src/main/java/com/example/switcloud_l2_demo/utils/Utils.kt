package com.example.switcloud_l2_demo.utils

import java.nio.charset.Charset
import java.nio.charset.CharacterCodingException

class Utils {

    companion object {

        // Helper function to convert a hex string to a ByteArray
        fun hexStringToByteArray(s: String): ByteArray {
            return s.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        }

        // Helper function to convert a ByteArray to a hex string
        fun byteArrayToHexString(byteArray: ByteArray): String {
            return byteArray.joinToString(separator = "") {
                "%02x".format(it)
            }
        }

        // Helper function to convert a hex string to an ASCII string
        fun hexStringToAsciiString(hexString: String): String {
            val byteArray = hexStringToByteArray(hexString)
            // Check if all bytes are valid ASCII characters (0-127)
            val isAscii = byteArray.all { it.toInt() in 0..127 }

            return if (isAscii) {
                try {
                    String(byteArray, Charsets.US_ASCII)
                } catch (e: CharacterCodingException) {
                    // Should not happen if isAscii check passes, but as a fallback
                    hexString
                }
            } else {
                hexString
            }
        }
    }
}
