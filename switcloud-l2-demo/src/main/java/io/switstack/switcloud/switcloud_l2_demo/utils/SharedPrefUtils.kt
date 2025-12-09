package io.switstack.switcloud.switcloud_l2_demo.utils

import android.content.Context
import io.switstack.switcloud.switcloud_l2_demo.R

class SharedPrefUtils(private val context: Context) {
    val sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    fun getTransactionCounter(): Int = sharedPref.getInt(context.getString(R.string.transaction_counter_key), 0)

    fun incrementAndPutTransactionCounter() {
        getTransactionCounter().let { counter ->
            with(sharedPref.edit()) {
                putInt(context.getString(R.string.transaction_counter_key), counter + 1)
                apply()
            }
        }
    }
}