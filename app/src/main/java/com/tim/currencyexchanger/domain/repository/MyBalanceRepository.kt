package com.tim.currencyexchanger.domain.repository

import com.tim.currencyexchanger.data.db.CurrencyExchangeDatabase
import com.tim.currencyexchanger.data.db.entity.BalanceEntity
import com.tim.currencyexchanger.data.model.CurrencyBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MyBalanceRepository {
    fun getMyBalance(): Flow<List<CurrencyBalance>>

    suspend fun addNewBalance(balance: CurrencyBalance)
}

class MyBalanceRepositoryImpl @Inject constructor(
    private val database: CurrencyExchangeDatabase,
) : MyBalanceRepository {

    override fun getMyBalance(): Flow<List<CurrencyBalance>> =
        database.balanceDao().getBalance().map {
            it.map { entity ->
                entity.convertToCurrencyBalance()
            }
        }

    override suspend fun addNewBalance(balance: CurrencyBalance) {
        database.balanceDao().insertBalanceToDB(
            BalanceEntity(amount = balance.amount, currency = balance.currency)
        )
    }
}