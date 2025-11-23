package io.switstack.switcloud.switcloud_l2_demo.utils

import android.R

class EmvUtils {

    companion object {

        // A simple map for common EMV tag names, sorted alphabetically by tag
        val emvTagNames = mapOf(
            "4F" to "AID",
            "50" to "Application Label",
            "57" to "Track 2 Equivalent Data",
            "5A" to "PAN",
            "5F20" to "Cardholder Name",
            "5F24" to "Application Expiration Date",
            "5F25" to "Application Effective Date",
            "5F2A" to "Transaction Currency Code",
            "5F34" to "Application Primary Account Number (PAN) Sequence Number",
            "82" to "Application Interchange Profile (AIP)",
            "84" to "DF Name",
            "95" to "TVR",
            "9A" to "Transaction Date",
            "9C" to "Transaction Type",
            "9F02" to "Amount",
            "9F03" to "Amount, Other (Numeric)",
            "9F06" to "Application Identifier (AID) – terminal",
            "9F07" to "Application Usage Control",
            "9F08" to "Application Version Number",
            "9F09" to "Application Version Number",
            "9F0D" to "Issuer Action Code – Default",
            "9F0E" to "Issuer Action Code – Denial",
            "9F0F" to "Issuer Action Code – Online",
            "9F10" to "Issuer Application Data",
            "9F12" to "Application Preferred Name",
            "9F15" to "Merchant Category Code",
            "9F16" to "Merchant Identifier",
            "9F1A" to "Terminal Country Code",
            "9F1C" to "Terminal Identification",
            "9F1E" to "Interface Device (IFD) Serial Number",
            "9F21" to "Transaction Time",
            "9F26" to "Application Cryptogram",
            "9F33" to "Terminal Capabilities",
            "9F34" to "Cardholder Verification Method (CVM) Results",
            "9F35" to "Terminal Type",
            "9F36" to "Application Transaction Counter (ATC)",
            "9F37" to "Unpredictable Number",
            "9F40" to "Additional Terminal Capabilities",
            "9F41" to "Transaction Sequence Counter",
            "9F4E" to "Merchant Name and Location",
            "9F53" to "Transaction Category Code",
            "9F5B" to "Issuer Script Results",
            "9F6C" to "Transaction Context",
            "9F6E" to "Third Party Data",
            "9F7C" to "Customer Exclusive Data",
            "9F7F" to "Card Production Life Cycle Data",
            "9F81" to "Amount, Authorised (Binary)",
            "9F82" to "Amount, Other (Binary)",
            "9F83" to "Currency Code, Transaction (Binary)",
            "9F84" to "Terminal Country Code (Binary)",
            "9F85" to "Transaction Date (Binary)",
            "9F86" to "Transaction Type (Binary)",
            "9F87" to "Unpredictable Number (Binary)",
            "9F88" to "Application Interchange Profile (Binary)",
            "9F89" to "Terminal Verification Results (Binary)",
            "9F8A" to "Application Transaction Counter (Binary)",
            "9F8B" to "Cardholder Verification Method (CVM) Results (Binary)",
            "9F8C" to "Application Cryptogram (Binary)",
            "9F8D" to "Dedicated File (DF) Name (Binary)",
            "9F8E" to "Application Primary Account Number (PAN) Sequence Number (Binary)",
            "9F8F" to "Track 2 Equivalent Data (Binary)",
            "9F90" to "Cardholder Name (Binary)",
            "9F91" to "Application Label (Binary)",
            "9F92" to "Application Preferred Name (Binary)",
            "9F93" to "Amount, Other (Binary)",
            "9F94" to "Terminal Capabilities (Binary)",
            "9F95" to "Terminal Type (Binary)",
            "9F96" to "Transaction Sequence Counter (Binary)",
            "9F97" to "Application Version Number (Binary)",
            "9F98" to "Third Party Data (Binary)",
            "9F99" to "Customer Exclusive Data (Binary)",
            "9F9A" to "Unpredictable Number (Binary)",
            "9F9B" to "Issuer Application Data (Binary)",
            "9F9C" to "Interface Device (IFD) Serial Number (Binary)",
            "9F9D" to "Application Identifier (AID) – terminal (Binary)",
            "9F9E" to "Application Identifier (AID) – card (Binary)",
            "9F9F" to "Application Expiration Date (Binary)",
            "9FA0" to "Application Effective Date (Binary)",
            "9FA1" to "Application Usage Control (Binary)",
            "9FA2" to "Application Version Number (Binary)",
            "9FA3" to "Issuer Action Code – Default (Binary)",
            "9FA4" to "Issuer Action Code – Denial (Binary)",
            "9FA5" to "Issuer Action Code – Online (Binary)",
            "9FA6" to "Merchant Category Code (Binary)",
            "9FA7" to "Merchant Identifier (Binary)",
            "9FA8" to "Terminal Identification (Binary)",
            "9FA9" to "Transaction Time (Binary)",
            "9FAA" to "Additional Terminal Capabilities (Binary)",
            "9FAB" to "Merchant Name and Location (Binary)",
            "9FAC" to "Transaction Category Code (Binary)",
            "9FAD" to "Issuer Script Results (Binary)",
            "9FAE" to "Transaction Context (Binary)",
            "9FAF" to "Card Production Life Cycle Data (Binary)",
            "DF8129" to "OPS"
        ).toSortedMap()


        fun isTagASCII(tag: String): Boolean {

            return tag == "50"
        }

        fun transactionTypeToString(transactionType: String): String {

            return when (transactionType) {
                "00" -> "Purchase"
                else -> "Unknown"
            }
        }
    }
}
