package io.switstack.switcloud.switcloud_l2_demo.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity(): Activity? {
    var context = this
    // The loop is necessary to unwrap multiple ContextWrappers, which can happen in different
    // configurations (e.g., with themes, view compatibility).
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        // Continue unwrapping the context
        context = context.baseContext
    }
    // If we've reached the base context and it's not an Activity, we're out of luck.
    // This can happen in previews, for example.
    return null
}

fun Context.readJsonFromAssets(fileName: String): String {
    return this.assets.open(fileName).bufferedReader().use { it.readText() }
}