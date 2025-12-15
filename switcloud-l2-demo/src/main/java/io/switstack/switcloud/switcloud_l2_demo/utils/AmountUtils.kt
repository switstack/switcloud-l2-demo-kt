package io.switstack.switcloud.switcloud_l2_demo.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class AmountUtils {

    companion object {

        /**
         * Converts a Double amount into a string formatted with exactly two decimal places.
         */
        fun Double.toCurrencyString(locale: Locale = Locale.US): String {
            val formatter = NumberFormat.getNumberInstance(locale) as DecimalFormat
            formatter.applyPattern("#0.00")
            return formatter.format(this)
        }

        /**
         * Custom function to process an amount string according to specific USD formatting rules:
         * 1. Removes leading "$" if present.
         * 2. Formats the resulting number to exactly two decimal places.
         * 3. Assumes a USD locale for decimal/grouping separators.
         *
         * @param amountString The input string which may contain a leading '$'.
         * @return The formatted amount string (e.g., "123.45") or null if parsing fails.
         */
        fun toUsdTwoDecimalString(amountString: String): String {
            // Remove leading "$" if present
            val cleanString = amountString.removePrefix("$")
            val amountDouble = NumberFormat.getInstance(Locale.US).parse(cleanString)?.toDouble()
            // Format the double value using the two-decimal format (fixed exponent of 2).
            return amountDouble!!.toCurrencyString(Locale.US)
        }
    }
}