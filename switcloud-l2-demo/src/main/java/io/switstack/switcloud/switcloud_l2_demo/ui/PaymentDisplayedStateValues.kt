package io.switstack.switcloud.switcloud_l2_demo.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class PaymentDisplayedStateValues(
    val imageConfig: ImageConfig?,
    val text: String,
    val buttonText: Int?,
    val colorTint: Color?
) {
    class ImageConfig(
        val drawable: Int,
        val width: Dp,
        val contentDescription: String
    )
}
