package io.switstack.switcloud.switcloud_l2_demo.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun isCompactDevice(): Boolean {
    val configuration = LocalConfiguration.current
    // A screen width of less than 600dp is generally considered a phone in portrait.
    return configuration.screenWidthDp.dp < 600.dp
}

@Composable
fun isSmallSquareScreen() = with(LocalConfiguration.current) {
    screenWidthDp.dp == screenHeightDp.dp && screenHeightDp == 480
}

@Composable
fun isSmallHeightScreen() = LocalConfiguration.current.screenHeightDp.dp < 600.dp