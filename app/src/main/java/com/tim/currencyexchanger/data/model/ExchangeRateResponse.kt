package com.tim.currencyexchanger.data.model

import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class ExchangeRateResponse(
    var base: String? = null,
    var date: String? = null,
    var rates: Map<String, Double>? = emptyMap(),
) {

    fun getExchangeRates(): List<ExchangeRate> {
        return rates?.map {
            ExchangeRate(
                currency = it.key,
                rate = BigDecimal.valueOf(it.value),
            )
        } ?: emptyList()
    }

}