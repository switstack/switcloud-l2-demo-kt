package io.switstack.switcloud.switcloud_l2_demo.data

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class NfcAntennaDeviceEnum(val land_x_offset: Dp, val land_y_offset: Dp, val port_x_offset: Dp, val port_y_offset: Dp) {
    PEPPERL_FUCHS_TABIND10AND(0.dp, 0.dp, 0.dp, 0.dp), //OONA 10 Android
    INEFI_ANDROID_G17( 450.dp, -100.dp, 0.dp, 0.dp), //FLYTECH POS317
    SUNMI_D3_MINI( 0.dp, 0.dp, 0.dp, 0.dp)
}