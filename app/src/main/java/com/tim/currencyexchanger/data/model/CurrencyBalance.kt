package com.tim.currencyexchanger.data.model

import java.math.BigDecimal

data class CurrencyBalance(
    val currency: String,
    val amount: BigDecimal,
)