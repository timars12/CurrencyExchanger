package com.tim.currencyexchanger.domain.usecase

import com.tim.currencyexchanger.data.model.CurrencyBalance
import com.tim.currencyexchanger.domain.repository.TransactionRepository
import com.tim.currencyexchanger.domain.repository.TransactionResult
import com.tim.currencyexchanger.domain.rules.TransactionRule
import java.math.BigDecimal
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val transactionsRule: Set<@JvmSuppressWildcards TransactionRule>,
) {

    suspend operator fun invoke(
        balance: List<CurrencyBalance>,
        sell: CurrencyBalance,
        buy: CurrencyBalance,
    ): TransactionResult {
        if (sell.currency == buy.currency) return TransactionResult.Error("Can not use the same currency")
        val currencyBalance = balance.first { it.currency == sell.currency }
        val transactionFee = getTransactionFee(sell)
        val sellBalanceWithFee = sell.amount + transactionFee
        if (sellBalanceWithFee > currencyBalance.amount) {
            return TransactionResult.Error("Not enough funds on balance")
        }
        return transactionRepository.saveTransactionToDB(
            sell.copy(amount = sellBalanceWithFee),
            buy,
            transactionFee
        )
    }

    private suspend fun getTransactionFee(sell: CurrencyBalance): BigDecimal {
        var totalFee: BigDecimal = BigDecimal.ZERO
        transactionsRule.forEach { rule -> rule.execute(sell)?.let { totalFee += it } }
        return totalFee
    }
}