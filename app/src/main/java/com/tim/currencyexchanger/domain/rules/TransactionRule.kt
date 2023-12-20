package com.tim.currencyexchanger.domain.rules

import com.tim.currencyexchanger.data.model.CurrencyBalance
import com.tim.currencyexchanger.domain.repository.TransactionRepository
import dagger.hilt.android.scopes.ViewModelScoped
import java.math.BigDecimal
import javax.inject.Inject

interface TransactionRule {
    suspend fun execute(transaction: CurrencyBalance): BigDecimal?
}

class ExchangeTransactionRule @Inject constructor(
    private val repository: TransactionRepository,
) : TransactionRule {
    private val fee = BigDecimal.valueOf(0.007)

    override suspend fun execute(transaction: CurrencyBalance): BigDecimal? {
        val transactionCount = repository.getCountOfAllTransaction()
        return when {
            transactionCount > 5 -> transaction.amount.multiply(fee)
            else -> null
        }
    }
}
