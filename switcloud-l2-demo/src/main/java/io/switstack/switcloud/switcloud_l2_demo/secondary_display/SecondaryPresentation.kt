package io.switstack.switcloud.switcloud_l2_demo.secondary_display


import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.Display
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import io.switstack.switcloud.switcloud_l2_demo.ui.theme.Switcloudl2demoktTheme

class SecondaryPresentation(
        context: Context,
        display: Display,
        val composable: @Composable () -> Unit
) : Presentation(context, display) {

    private val lifecycleOwner = PresentationLifecycleOwner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleOwner.performRestore(savedInstanceState)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        val composeView = ComposeView(context).apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setContent {
                Switcloudl2demoktTheme {
                    composable()
                }
            }
        }
        setContentView(composeView)
    }

    override fun onStart() {
        super.onStart()
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onStop() {
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        super.onStop()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleOwner.viewModelStore.clear()
    }
}

private class PresentationLifecycleOwner : LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val viewModelStore = ViewModelStore()

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    fun performRestore(savedState: Bundle?) {
        savedStateRegistryController.performRestore(savedState)
    }

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        lifecycleRegistry.handleLifecycleEvent(event)
    }
}