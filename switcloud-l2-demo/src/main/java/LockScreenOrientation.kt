import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import io.switstack.switcloud.switcloud_l2_demo.utils.isCompactDevice

@Composable
fun LockScreenOrientation() {
    val context = LocalContext.current
    val isPhone = isCompactDevice()

    // This effect will run whenever isTablet changes (which is only on first composition)
    LaunchedEffect(isPhone) {
        val activity = context as? Activity ?: return@LaunchedEffect

        activity.requestedOrientation = if (isPhone) {
            // For phones, lock to portrait
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // For tablets, allow sensor-based rotation
            ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
    }
}