package io.switstack.switcloud.switcloud_l2_demo.utils

import io.switstack.switcloud.switcloud_l2_demo.data.EmvTagEnum
import io.switstack.switcloud.switcloud_l2_demo.data.EmvTagEnum.Companion.fromTag
import io.switstack.switcloud.switcloud_l2_demo.data.OPSVerdictEnum

class EmvUtils {
    companion object {

        fun getTagLabel(tag: String) = when (val tagEnum = fromTag(tag.uppercase())) {
            EmvTagEnum.TAG_DF8129 -> "Status"
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

                EmvTagEnum.TAG_50 -> ByteArrayHexStringUtils.hexStringToAsciiString(value)
                EmvTagEnum.TAG_DF8129 -> getOPSVerdictLabel(value)
                else -> value
            }
        }

        fun getOPSVerdict(hexValueString: String): OPSVerdictEnum {
            val OPSValueByteArray = ByteArrayHexStringUtils.hexStringToByteArray(hexValueString)
            // check if CVM is required
            if (OPSValueByteArray.size >= 4 && OPSValueByteArray[3] == 0x20.toByte()) {
                // Show PIN entry
                return OPSVerdictEnum.PIN_REQUIRED
            } else {
                // PIN entry not required
                if (OPSValueByteArray.isNotEmpty()
                    && OPSValueByteArray[0] == 0x10.toByte() // Approved
                    || OPSValueByteArray[0] == 0x30.toByte() // Online request
                ) {
                    return OPSVerdictEnum.SUCCESS
                } else {
                    return OPSVerdictEnum.FAILURE
                }
            }
        }

        fun getOPSVerdictLabel(hexString: String): String = when (getOPSVerdict(hexString)) {
            OPSVerdictEnum.SUCCESS -> "APPROVED"
            OPSVerdictEnum.PIN_REQUIRED -> "APPROVED" // Only in case of the demo app
            OPSVerdictEnum.FAILURE -> "DECLINED"
        }
    }
}
