package io.switstack.switcloud.switcloud_l2_demo.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for creating a PaymentViewModel with a constructor that takes an Activity.
 * This is the standard pattern for passing parameters to a ViewModel's constructor.
 */
class PaymentViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is our PaymentViewModel
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            // If it is, create and return an instance, passing the activity to its constructor.
            return PaymentViewModel(activity) as T
        }
        // If it's a different ViewModel, throw an exception.
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}