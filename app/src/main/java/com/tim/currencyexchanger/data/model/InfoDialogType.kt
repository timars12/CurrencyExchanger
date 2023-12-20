package com.tim.currencyexchanger.data.model

import com.tim.currencyexchanger.ui.EnterAmountWithCurrency

sealed interface InfoDialogType {
    data class TransactionSuccess(
        val fee: String,
        val sell: EnterAmountWithCurrency,
        val buy: CurrencyBalance,
    ) : InfoDialogType

    data class Error(val text: String? = null) : InfoDialogType
    data object Hide : InfoDialogType
}