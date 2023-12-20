package com.tim.currencyexchanger.domain.network

import com.tim.currencyexchanger.data.model.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyExchangeApiService {

    @GET("currency-exchange-rates")
    suspend fun getExchangeRates(): Response<ExchangeRateResponse>
}