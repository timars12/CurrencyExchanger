package com.tim.currencyexchanger.data.model

sealed interface CurrencyBottomSheetState {
    data object Hide : CurrencyBottomSheetState
    data object ChangeReceiveCurrency : CurrencyBottomSheetState
    data object ChangeSellCurrency : CurrencyBottomSheetState
}