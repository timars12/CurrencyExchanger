package com.tim.currencyexchanger.domain.repository

import com.tim.currencyexchanger.data.model.ExchangeData
import com.tim.currencyexchanger.data.model.Result
import com.tim.currencyexchanger.domain.network.CurrencyExchangeApiService
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

interface CurrencyExchangeRepository {
    fun getExchangeRates(): Flow<Result<ExchangeData>>
}

private val SYNC_PERIOD = 5.seconds

class CurrencyExchangeRepositoryImpl @Inject constructor(private val apiService: CurrencyExchangeApiService) :
    CurrencyExchangeRepository {
    override fun getExchangeRates(): Flow<Result<ExchangeData>> = flow {
        while (currentCoroutineContext().isActive) {
            try {
                val response = apiService.getExchangeRates()
                val data = when {
                    response.code() == 200 -> {
                        Result.Success(
                            ExchangeData(
                                base = response.body()?.base!!,
                                date = response.body()?.date!!,
                                rates = response.body()?.getExchangeRates()!!,
                            )
                        )
                    }
                    else -> Result.Error(Exception("Server error"))
                }
                emit(data)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e))
            }
            delay(SYNC_PERIOD)
        }
    }

}