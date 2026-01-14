package io.switstack.switcloud.switcloud_l2_demo.utils

import io.switstack.switcloud.switcloud_l2_demo.data.EmvTagEnum
import io.switstack.switcloud.switcloud_l2_demo.data.EmvTagEnum.Companion.fromTag
import io.switstack.switcloud.switcloud_l2_demo.data.OPSVerdictEnum

object EmvUtils {
    fun getTagLabel(tag: String) = when (val tagEnum = fromTag(tag.uppercase())) {
        EmvTagEnum.TAG_DF8129,
        EmvTagEnum.TAG_9F8210 -> "Status"

        else -> tagEnum?.tagName ?: tag.uppercase()
    }

    fun getValueLabel(tag: String, value: String): String {
        val value = value.uppercase() // "value" is now shadowed and is always uppercase
        return when (fromTag(tag.uppercase())) {
            EmvTagEnum.TAG_9C -> {
                when (value) {
                    "00" -> "Purchase"
                    else -> "Unknown"
                }
            }

            EmvTagEnum.TAG_5F20,
            EmvTagEnum.TAG_50 -> ByteArrayHexStringUtils.hexStringToAsciiString(value)

            EmvTagEnum.TAG_DF8129,
            EmvTagEnum.TAG_9F8210 -> getOPSVerdictLabel(value)

            else -> value
        }
    }

    fun getOPSVerdict(hexValueString: String): OPSVerdictEnum {
        val OPSValueByteArray = ByteArrayHexStringUtils.hexStringToByteArray(hexValueString)
        // check if CVM is required
        return if (OPSValueByteArray.size >= 4 && OPSValueByteArray[3] == 0x20.toByte()) {
            // Show PIN entry
            OPSVerdictEnum.PIN_REQUIRED
        } else {
            // PIN entry not required
            if (OPSValueByteArray.isNotEmpty() &&
                (
                    OPSValueByteArray[0] == 0x10.toByte() || // Approved
                        OPSValueByteArray[0] == 0x30.toByte()
                    ) // Online request
            ) {
                OPSVerdictEnum.APPROVED
            } else {
                OPSVerdictEnum.DECLINED
            }
        }
    }

    fun getOPSStatus(hexValueString: String): String? {
        val OPSValueByteArray = ByteArrayHexStringUtils.hexStringToByteArray(hexValueString)
        // check OPS status
        if (OPSValueByteArray.isNotEmpty()) {
            return when (OPSValueByteArray[0]) {
                0x10.toByte() -> "Approved"
                0x20.toByte() -> "Declined"
                0x30.toByte() -> "Online request"
                0x40.toByte() -> "End application"
                0x50.toByte() -> "Select next"
                0x60.toByte() -> "Try another interface"
                0x60.toByte() -> "Try again"
                else -> "Declined"
            }
        }
        return null
    }

    fun getErrorIndication(hexValueString: String): String? {
        val EIValueByteArray = ByteArrayHexStringUtils.hexStringToByteArray(hexValueString)

        if (EIValueByteArray.size >= 2) {
            return when (EIValueByteArray[1]) {
                0x00.toByte() -> null // if OK no error indication associated
                0x01.toByte() -> "Card data missing"
                0x02.toByte() -> "CAM failed"
                0x03.toByte() -> "Status bytes"
                0x04.toByte() -> "Parsing error"
                0x05.toByte() -> "Max limit exceeded"
                0x06.toByte() -> "Card data error"
                0x07.toByte() -> "Magstripe not supported"
                0x08.toByte() -> "No PPSE"
                0x09.toByte() -> "PPSE fault"
                0x0A.toByte() -> "Empty candidate list"
                else -> "Terminal data error"
            }
        }
        return null
    }

    fun getOPSVerdictLabel(hexString: String): String = when (getOPSVerdict(hexString)) {
        OPSVerdictEnum.APPROVED -> OPSVerdictEnum.APPROVED.name
        OPSVerdictEnum.PIN_REQUIRED -> OPSVerdictEnum.APPROVED.name // Only in case of the demo app
        OPSVerdictEnum.DECLINED -> OPSVerdictEnum.DECLINED.name
    }

    fun getOPSVerdictLabel(success: Boolean): String = if (success) {
        OPSVerdictEnum.APPROVED.name
    } else {
        OPSVerdictEnum.DECLINED.name
    }
}
