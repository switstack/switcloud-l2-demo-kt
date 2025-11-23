package io.switstack.switcloud.switcloud_l2_demo.utils

class MokaConfig {

    companion object {

        val readerParams = byteArrayOf(
            // Trace (on)
            0xDF.toByte(), 0xA0.toByte(), 0x18, 0x01, 0x01,
            // Polling timeout (30 sec)
            0xDF.toByte(), 0xA0.toByte(), 0x07, 0x01, 0x1e,
            // Timeout interfaces detection (10 sec)
            0xDF.toByte(), 0xA0.toByte(), 0x08, 0x01, 0x1e,
        )
    }
}
