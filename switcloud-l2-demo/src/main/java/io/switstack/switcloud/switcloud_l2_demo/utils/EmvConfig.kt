package io.switstack.switcloud.switcloud_l2_demo.utils

object EmvConfig {
    // Entry point configuration as ByteArray
    val entryPointConfiguration = byteArrayOf(
        0xDF.toByte(),
        0xA0.toByte(),
        0x25,
        0x01,
        0x00
    )
}
