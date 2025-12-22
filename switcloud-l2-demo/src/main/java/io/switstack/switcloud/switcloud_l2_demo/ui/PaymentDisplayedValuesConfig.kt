package io.switstack.switcloud.switcloud_l2_demo.ui

import androidx.compose.ui.unit.Dp

data class PaymentDisplayedValuesConfig(
    val imageConfig: ImageConfig?,
    val text: String,
    val buttonText: Int?

) {
    class ImageConfig(
            val drawable: Int,
            val width: Dp,
            val contentDescription: String
    )

}
