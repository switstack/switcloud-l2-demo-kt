package io.switstack.switcloud.switcloud_l2_demo.utils

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, modification: Modifier.() -> Modifier) =
    if (condition) modification() else this

fun Modifier.thenIf(condition: Boolean, modifier: Modifier) =
    if (condition) this.then(modifier) else this