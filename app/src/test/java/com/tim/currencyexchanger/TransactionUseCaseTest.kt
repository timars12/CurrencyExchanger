package com.tim.currencyexchanger

import com.tim.currencyexchanger.data.model.CurrencyBalance
import com.tim.currencyexchanger.domain.repository.TransactionRepository
import com.tim.currencyexchanger.domain.repository.TransactionResult
import com.tim.currencyexchanger.domain.usecase.TransactionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TransactionUseCaseTest {
    private val mockTransactionRepository = mockk<TransactionRepository>()

    @Test
    fun `perform transaction with enough funds and different currencies`() {
        runTest {
            val balance = listOf(CurrencyBalance("USD", BigDecimal(100)))
            val sell = CurrencyBalance("USD", BigDecimal(50))
            val buy = CurrencyBalance("EUR", BigDecimal(30))
            val givenTransactionFee = BigDecimal.ZERO

            coEvery {
                mockTransactionRepository.saveTransactionToDB(sell, buy, givenTransactionFee)
            } returns TransactionResult.Success(givenTransactionFee.toPlainString())

            val transactionUseCase = TransactionUseCase(mockTransactionRepository, setOf())
            val result = transactionUseCase(balance, sell, buy)

            assertEquals(TransactionResult.Success(givenTransactionFee.toPlainString()), result)
            coVerify(exactly = 1) {
                mockTransactionRepository.saveTransactionToDB(sell, buy, givenTransactionFee)
            }
        }
    }

    @Test
    fun `perform transaction with insufficient funds`() {
        runTest {
            val balance = listOf(CurrencyBalance("USD", BigDecimal(10)))
            val sell = CurrencyBalance("USD", BigDecimal(50))
            val buy = CurrencyBalance("EUR", BigDecimal(30))

            val transactionUseCase = TransactionUseCase(mockTransactionRepository, setOf())
            val result = transactionUseCase(balance, sell, buy)

            assertEquals(TransactionResult.Error("Not enough funds on balance"), result)
        }
    }

    @Test
    fun `perform transaction with same currency`() {
        runTest {
            val balance = listOf(CurrencyBalance("USD", BigDecimal(10)))
            val sell = CurrencyBalance("USD", BigDecimal(50))
            val buy = CurrencyBalance("USD", BigDecimal(30))

            val transactionUseCase = TransactionUseCase(mockTransactionRepository, setOf())
            val result = transactionUseCase(balance, sell, buy)

            assertEquals(TransactionResult.Error("Can not use the same currency"), result)
        }
    }
}