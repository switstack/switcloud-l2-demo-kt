package com.example.switcloud_l2_demo.utils

class EmvConfig {

    companion object {

        // Entry point configuration as ByteArray
        val entryPointConfiguration = byteArrayOf(
            0xDF.toByte(), 0xA0.toByte(), 0x25, 0x01, 0x00
        )

        // Mastercard combination
        val combinationMastercard = byteArrayOf(
            0xE1.toByte(), 0x32,
            // AID
            0x9F.toByte(), 0x06, 0x07, 0xA0.toByte(), 0x00, 0x00, 0x00, 0x04, 0x10, 0x10,
            // Kernel ID
            0xDF.toByte(), 0x81.toByte(), 0x0C, 0x01, 0x02,
            // Partial Selection flag
            0xDF.toByte(), 0xA0.toByte(), 0x10, 0x01, 0x01,
            // Transaction Type
            0x9c.toByte(), 0x01, 0x00,
            // Configuration container + length
            0xE2.toByte(), 0x19,
            // Reader Contactless Floor Limit
            0xDF.toByte(), 0x81.toByte(), 0x23, 0x06, 0x00, 0x00, 0x00, 0x00, 0x16, 0x00,
            // Reader Contactless Transaction Limit (No On-Device CVM)
            0xDF.toByte(), 0x81.toByte(), 0x24, 0x06, 0x00, 0x00, 0x00, 0x00, 0x50, 0x00,
            // Security Capability
            0xDF.toByte(), 0x81.toByte(), 0x1F, 0x01, 0x08
        )

        // Visa combination
        val combinationVisa = byteArrayOf(
            0xE1.toByte(), 0x52,
            // Kernel ID
            0xDF.toByte(), 0x81.toByte(), 0x0C, 0x01, 0x03,
            // AID
            0x9F.toByte(), 0x06, 0x07, 0xA0.toByte(), 0x00, 0x00, 0x00, 0x03, 0x10, 0x10,
            // Partial Selection flag
            0xDF.toByte(), 0xA0.toByte(), 0x10, 0x01, 0x01,
            // Transaction Type
            0x9c.toByte(), 0x01, 0x00,
            // Configuration container + length
            0xE2.toByte(), 0x45,
            // TTQ
            0x9F.toByte(), 0x66, 0x04, 0x27, 0x00, 0x40, 0x00,
            0x9F.toByte(), 0x1B, 0x04, 0x00, 0x00, 0x27, 0x10,
            0x9F.toByte(), 0x82.toByte(), 0x0D, 0x06, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00,
            0x9F.toByte(), 0x82.toByte(), 0x0E, 0x06, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00,
            0xDF.toByte(), 0xA0.toByte(), 0x20, 0x06, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00,
            0xDF.toByte(), 0xA0.toByte(), 0x14, 0x01, 0x01,
            0xDF.toByte(), 0xA0.toByte(), 0x15, 0x01, 0x01,
            0xDF.toByte(), 0xA0.toByte(), 0x16, 0x01, 0x01,
            0xDF.toByte(), 0xA0.toByte(), 0x17, 0x01, 0x01,
            0x9F.toByte(), 0x1A, 0x02, 0x08, 0x40
        )

        // CA Key
        val caKey = byteArrayOf(
            0xDF.toByte(),
            0xA0.toByte(),
            0x23,
            0x82.toByte(),
            0x01,
            0x22,
            // RID
            0xA0.toByte(),
            0x00,
            0x00,
            0x00,
            0x04,
            // Index
            0x00,
            // Hash indicator (RFU)
            0x00,
            // Algo indicator (RFU)
            0x00,
            // Modulus length
            0x00,
            0xA0.toByte(),
            // Modulus
            0x9C.toByte(),
            0x6B,
            0xE5.toByte(),
            0xAD.toByte(),
            0xB1.toByte(),
            0x0B,
            0x4B,
            0xE3.toByte(),
            0xDC.toByte(),
            0xE2.toByte(),
            0x09,
            0x9B.toByte(),
            0x4B,
            0x21,
            0x06,
            0x72,
            0xB8.toByte(),
            0x96.toByte(),
            0x56,
            0xEB.toByte(),
            0xA0.toByte(),
            0x91.toByte(),
            0x20,
            0x4F,
            0x61,
            0x3E,
            0xCC.toByte(),
            0x62,
            0x3B,
            0xED.toByte(),
            0xC9.toByte(),
            0xC6.toByte(),
            0xD7.toByte(),
            0x7B,
            0x66,
            0x0E,
            0x8B.toByte(),
            0xAE.toByte(),
            0xEA.toByte(),
            0x7F,
            0x7C,
            0xE3.toByte(),
            0x0F,
            0x1B,
            0x15,
            0x38,
            0x79,
            0xA4.toByte(),
            0xE3.toByte(),
            0x64,
            0x59,
            0x34,
            0x3D,
            0x1F,
            0xE4.toByte(),
            0x7A,
            0xCD.toByte(),
            0xBD.toByte(),
            0x41,
            0xFC.toByte(),
            0xD7.toByte(),
            0x10,
            0x03,
            0x0C,
            0x2B,
            0xA1.toByte(),
            0xD9.toByte(),
            0x46,
            0x15,
            0x97.toByte(),
            0x98.toByte(),
            0x2C,
            0x6E,
            0x1B,
            0xDD.toByte(),
            0x08,
            0x55,
            0x4B,
            0x72,
            0x6F,
            0x5E,
            0xFF.toByte(),
            0x79,
            0x13,
            0xCE.toByte(),
            0x59,
            0xE7.toByte(),
            0x9E.toByte(),
            0x35,
            0x72,
            0x95.toByte(),
            0xC3.toByte(),
            0x21,
            0xE2.toByte(),
            0x6D,
            0x0B,
            0x8B.toByte(),
            0xE2.toByte(),
            0x70,
            0xA9.toByte(),
            0x44,
            0x23,
            0x45,
            0xC7.toByte(),
            0x53,
            0xE2.toByte(),
            0xAA.toByte(),
            0x2A,
            0xCF.toByte(),
            0xC9.toByte(),
            0xD3.toByte(),
            0x08,
            0x50,
            0x60,
            0x2F,
            0xE6.toByte(),
            0xCA.toByte(),
            0xC0.toByte(),
            0x0C,
            0x6D,
            0xDF.toByte(),
            0x6B,
            0x8D.toByte(),
            0x9D.toByte(),
            0x9B.toByte(),
            0x48,
            0x79,
            0x0B,
            0x82.toByte(),
            0x6B,
            0x04,
            0x2A,
            0x07,
            0xF0.toByte(),
            0xE5.toByte(),
            0xAE.toByte(),
            0x52,
            0x6A,
            0x3D,
            0x3C,
            0x4D,
            0x22,
            0xC7.toByte(),
            0x2B,
            0x9E.toByte(),
            0xAA.toByte(),
            0x52,
            0xEE.toByte(),
            0xD8.toByte(),
            0x89.toByte(),
            0x38,
            0x66,
            0xF8.toByte(),
            0x66,
            0x38,
            0x7A,
            0xC0.toByte(),
            0x5A,
            0x13,
            0x99.toByte(),
            // Padding zeros
            *ByteArray(96) { 0x00 },
            // Exponent length
            0x01,
            // Exponent
            0x03,
            0x00,
            0x00,
            // Hash
            *ByteArray(20) { 0x00 }
        )

        // Transaction Related Data
        val trd = byteArrayOf(
            // Transaction type
            0x9c.toByte(), 0x01, 0x00
        )
    }
}
