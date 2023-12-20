package com.tim.currencyexchanger.data.model

import java.math.BigDecimal

data class ExchangeRate(
    val currency: String,
    val rate: BigDecimal,
)