package com.tim.currencyexchanger.data.model

import androidx.compose.runtime.Stable
import java.math.BigDecimal

@Stable
data class CurrencyBalance(
    val currency: String,
    val amount: BigDecimal,
)