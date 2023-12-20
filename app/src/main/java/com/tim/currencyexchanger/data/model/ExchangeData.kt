package com.tim.currencyexchanger.data.model

data class ExchangeData(
    val base: String,
    val date: String,
    val rates: List<ExchangeRate>,
)
