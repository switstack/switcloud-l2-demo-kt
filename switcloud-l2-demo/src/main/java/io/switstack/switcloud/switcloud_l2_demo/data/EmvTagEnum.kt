package io.switstack.switcloud.switcloud_l2_demo.data

enum class EmvTagEnum(val tagName: String) {
    TAG_4F("AID"),
    TAG_50("Application Label"),
    TAG_57("Track 2 Equivalent Data"),
    TAG_5A("PAN"),
    TAG_5F20("Cardholder Name"),
    TAG_5F24("Application Expiration Date"),
    TAG_5F25("Application Effective Date"),
    TAG_5F2A("Transaction Currency Code"),
    TAG_5F34("Application Primary Account Number (PAN) Sequence Number"),
    TAG_82("Application Interchange Profile (AIP)"),
    TAG_84("DF Name"),
    TAG_95("TVR"),
    TAG_9A("Transaction Date"),
    TAG_9C("Transaction Type"),
    TAG_9F02("Amount"),
    TAG_9F03("Amount, Other (Numeric)"),
    TAG_9F06("Application Identifier (AID) – terminal"),
    TAG_9F07("Application Usage Control"),
    TAG_9F08("Application Version Number"),
    TAG_9F09("Application Version Number"),
    TAG_9F0D("Issuer Action Code – Default"),
    TAG_9F0E("Issuer Action Code – Denial"),
    TAG_9F0F("Issuer Action Code – Online"),
    TAG_9F10("Issuer Application Data"),
    TAG_9F12("Application Preferred Name"),
    TAG_9F15("Merchant Category Code"),
    TAG_9F16("Merchant Identifier"),
    TAG_9F1A("Terminal Country Code"),
    TAG_9F1C("Terminal Identification"),
    TAG_9F1E("Interface Device (IFD) Serial Number"),
    TAG_9F21("Transaction Time"),
    TAG_9F26("Application Cryptogram"),
    TAG_9F27("CID"),
    TAG_9F33("Terminal Capabilities"),
    TAG_9F34("Cardholder Verification Method (CVM) Results"),
    TAG_9F35("Terminal Type"),
    TAG_9F36("Application Transaction Counter (ATC)"),
    TAG_9F37("Unpredictable Number"),
    TAG_9F40("Additional Terminal Capabilities"),
    TAG_9F41("Transaction Sequence Counter"),
    TAG_9F4E("Merchant Name and Location"),
    TAG_9F53("Transaction Category Code"),
    TAG_9F5B("Issuer Script Results"),
    TAG_9F6C("Transaction Context"),
    TAG_9F6E("Third Party Data"),
    TAG_9F7C("Customer Exclusive Data"),
    TAG_9F7F("Card Production Life Cycle Data"),
    TAG_9F81("Amount, Authorised (Binary)"),
    TAG_9F82("Amount, Other (Binary)"),
    TAG_9F83("Currency Code, Transaction (Binary)"),
    TAG_9F84("Terminal Country Code (Binary)"),
    TAG_9F85("Transaction Date (Binary)"),
    TAG_9F86("Transaction Type (Binary)"),
    TAG_9F87("Unpredictable Number (Binary)"),
    TAG_9F88("Application Interchange Profile (Binary)"),
    TAG_9F89("Terminal Verification Results (Binary)"),
    TAG_9F8A("Application Transaction Counter (Binary)"),
    TAG_9F8B("Cardholder Verification Method (CVM) Results (Binary)"),
    TAG_9F8C("Application Cryptogram (Binary)"),
    TAG_9F8D("Dedicated File (DF) Name (Binary)"),
    TAG_9F8E("Application Primary Account Number (PAN) Sequence Number (Binary)"),
    TAG_9F8F("Track 2 Equivalent Data (Binary)"),
    TAG_9F90("Cardholder Name (Binary)"),
    TAG_9F91("Application Label (Binary)"),
    TAG_9F92("Application Preferred Name (Binary)"),
    TAG_9F93("Amount, Other (Binary)"),
    TAG_9F94("Terminal Capabilities (Binary)"),
    TAG_9F95("Terminal Type (Binary)"),
    TAG_9F96("Transaction Sequence Counter (Binary)"),
    TAG_9F97("Application Version Number (Binary)"),
    TAG_9F98("Third Party Data (Binary)"),
    TAG_9F99("Customer Exclusive Data (Binary)"),
    TAG_9F9A("Unpredictable Number (Binary)"),
    TAG_9F9B("Issuer Application Data (Binary)"),
    TAG_9F9C("Interface Device (IFD) Serial Number (Binary)"),
    TAG_9F9D("Application Identifier (AID) – terminal (Binary)"),
    TAG_9F9E("Application Identifier (AID) – card (Binary)"),
    TAG_9F9F("Application Expiration Date (Binary)"),
    TAG_9FA0("Application Effective Date (Binary)"),
    TAG_9FA1("Application Usage Control (Binary)"),
    TAG_9FA2("Application Version Number (Binary)"),
    TAG_9FA3("Issuer Action Code – Default (Binary)"),
    TAG_9FA4("Issuer Action Code – Denial (Binary)"),
    TAG_9FA5("Issuer Action Code – Online (Binary)"),
    TAG_9FA6("Merchant Category Code (Binary)"),
    TAG_9FA7("Merchant Identifier (Binary)"),
    TAG_9FA8("Terminal Identification (Binary)"),
    TAG_9FA9("Transaction Time (Binary)"),
    TAG_9FAA("Additional Terminal Capabilities (Binary)"),
    TAG_9FAB("Merchant Name and Location (Binary)"),
    TAG_9FAC("Transaction Category Code (Binary)"),
    TAG_9FAD("Issuer Script Results (Binary)"),
    TAG_9FAE("Transaction Context (Binary)"),
    TAG_9FAF("Card Production Life Cycle Data (Binary)"),
    TAG_DF8129("OPS"),
    UNKNOWN("Unknown Tag");

    /**
     * A property to get the hex string representation of the tag.
     * For example, for TAG_4F, it returns "4F".
     * It returns an empty string for the UNKNOWN tag.
     */
    val hexTag: String
        get() = if (this != UNKNOWN) {
            this.name.replace("TAG_", "")
        } else {
            ""
        }

    companion object {
        private val map = entries.associateBy { it.name.replace("TAG_", "") }

        /**
         * Finds the corresponding enum constant for a given hex tag string.
         * For example, "4F" would return TicketTagsEnum.TAG_4F.
         * Returns null if no match is found.
         */
        fun fromTag(tag: String) = map[tag]
    }

}
