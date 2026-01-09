package io.switstack.switcloud.switcloud_l2_demo.secondary_display

import android.app.Activity
import android.hardware.display.DisplayManager
import android.view.Display
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

class SecondaryDisplayManager(private val activity: Activity) {

    private var secondaryPresentation: SecondaryPresentation? = null

    fun release() {
        secondaryPresentation?.dismiss()
        secondaryPresentation = null
    }

    fun secondaryDisplayExists() = secondaryDisplay != null

    private val secondaryDisplay: Display?
        get() {
            val displayManager = activity.getSystemService(DisplayManager::class.java)

            return displayManager?.displays?.find { display ->
                println("Screen: $display")

                val isSecure = (display.flags and Display.FLAG_SECURE) != 0
                val supportsProtected = (display.flags and Display.FLAG_SUPPORTS_PROTECTED_BUFFERS) != 0
                val isPresentation = (display.flags and Display.FLAG_PRESENTATION) != 0

                // First real second screen
                isSecure && isPresentation
            }
        }

    fun show(content: @Composable () -> Unit) {
        secondaryPresentation?.apply {
            if(isShowing) {
                secondaryPresentation?.dismiss()
                secondaryPresentation = null
            }
        }
        secondaryDisplay?.let { display ->
            secondaryPresentation = SecondaryPresentation(activity, display, content).apply {
                setOnDismissListener { secondaryPresentation = null }
                show()
            }
        }
    }
}

val LocalSecondaryDisplayManager = staticCompositionLocalOf<SecondaryDisplayManager?> { null }
