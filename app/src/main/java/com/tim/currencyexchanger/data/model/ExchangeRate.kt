package com.tim.currencyexchanger.data.model

import androidx.compose.runtime.Stable
import java.math.BigDecimal

@Stable
data class ExchangeRate(
    val currency: String,
    val rate: BigDecimal,
)