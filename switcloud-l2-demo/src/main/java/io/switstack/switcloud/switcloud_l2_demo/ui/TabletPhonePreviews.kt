package io.switstack.switcloud.switcloud_l2_demo.ui

import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Tablet landscape",
    group = "tablet",
    device = TABLET
)
@Preview(
    name = "Tablet portrait",
    group = "tablet",
    device = TABLET,
    widthDp = 800,
    heightDp = 1280
)
@Preview(
    name = "Phone portrait",
    group = "Phone",
    device = PHONE
)
@Preview(
    name = "Newland terminal",
    group = "Phone",
    device = PHONE,
    widthDp = 480,
    heightDp = 480
)
@Preview(
    name = "Sunmi terminal small screen",
    group = "Phone",
    device = PHONE,
    widthDp = 800,
    heightDp = 480
)
@Preview(
    name = "AuthSignal screen",
    group = "Phone",
    device = PHONE,
    widthDp = 360,
    heightDp = 800
)
annotation class TabletPhonePreviews