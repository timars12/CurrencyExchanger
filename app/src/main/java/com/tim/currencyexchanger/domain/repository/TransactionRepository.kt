package com.tim.currencyexchanger.domain.repository

import androidx.room.withTransaction
import com.tim.currencyexchanger.data.db.CurrencyExchangeDatabase
import com.tim.currencyexchanger.data.db.entity.BalanceEntity
import com.tim.currencyexchanger.data.db.entity.TransactionEntity
import com.tim.currencyexchanger.data.model.CurrencyBalance
import java.math.BigDecimal
import javax.inject.Inject

interface TransactionRepository {
    suspend fun getCountOfAllTransaction(): Int

    suspend fun saveTransactionToDB(
        sellWithFee: CurrencyBalance,
        buy: CurrencyBalance,
        transactionFee: BigDecimal,
    ): TransactionResult
}

class TransactionRepositoryImpl @Inject constructor(
    private val database: CurrencyExchangeDatabase,
) : TransactionRepository {

    override suspend fun getCountOfAllTransaction(): Int =
        database.transactionDao().getCountOfAllTransaction()

    override suspend fun saveTransactionToDB(
        sellWithFee: CurrencyBalance,
        buy: CurrencyBalance,
        transactionFee: BigDecimal
    ): TransactionResult {
        val isSuccess = database.withTransaction {
            val balanceBefore = database.balanceDao().getBalanceByCurrency(sellWithFee.currency)
                ?: return@withTransaction -1L

            database.balanceDao().updateBalance(
                balanceBefore.copy(
                    amount = balanceBefore.amount.minus(sellWithFee.amount)
                )
            )

            when (val buyCurrencyExist = database.balanceDao().getBalanceByCurrency(buy.currency)) {
                null -> {
                    database.balanceDao().insertBalanceToDB(
                        BalanceEntity(amount = buy.amount, currency = buy.currency)
                    )
                }
                else -> database.balanceDao().updateBalance(
                    buyCurrencyExist.copy(
                        amount = buyCurrencyExist.amount.plus(buy.amount)
                    )
                )
            }

            return@withTransaction database.transactionDao().insertTransactionToDB(
                TransactionEntity(
                    currencySell = sellWithFee.currency,
                    currencyBuy = buy.currency
                )
            )
        }.run { this >= 0 }

        return if (isSuccess) TransactionResult.Success(transactionFee.toPlainString()) else TransactionResult.Error()
    }
}

sealed interface TransactionResult {
    data class Success(val transactionFee: String) : TransactionResult
    data class Error(val message: String? = null) : TransactionResult
}