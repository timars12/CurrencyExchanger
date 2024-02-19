package com.tim.currencyexchanger.ui.currencyExchang.composable

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object AmountValidation {
    private val decimalFormat = DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US))

    /**
     * If [amount] only include digits, it returns a formatted amount.
     * Otherwise returns plain input as it is
     */
    fun formatAmount(amount: String): String = try {
        decimalFormat.format(amount.toDouble())
    } catch (e: Exception) {
        amount
    }
}