package io.switstack.switcloud.switcloud_l2_demo.utils

import io.switstack.switcloud.switcloud_l2_demo.BuildConfig
import java.util.Locale.getDefault

object FlavorUtils {
    fun getFlavorTarget(): FlavorTargetEnum = FlavorTargetEnum.valueOf(BuildConfig.FLAVOR_target.uppercase(getDefault()))
    fun getFlavorMode(): FlavorModeEnum = FlavorModeEnum.valueOf(BuildConfig.FLAVOR_mode.uppercase(getDefault()))
}