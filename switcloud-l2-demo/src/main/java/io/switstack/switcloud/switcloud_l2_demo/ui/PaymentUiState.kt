package io.switstack.switcloud.switcloud_l2_demo.ui

data class PaymentUiState(
        val initialized: Boolean = false,
        val showPinEntry: Boolean = false,
        val tlvString: String? = null,
        val success: Boolean? = null,
        val errorMessageResource: Int? = null,
        val declinedOpsStatusAndErrorIndicationMessage: String? = null,
)
