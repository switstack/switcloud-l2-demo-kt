package io.switstack.switcloud.switcloud_l2_demo.ui

data class PaymentUiState(
    val initialized: Boolean = false,
    val tlvString: String? = null,
    val success: Boolean = false,
    val errorMessage: String? = null
)
