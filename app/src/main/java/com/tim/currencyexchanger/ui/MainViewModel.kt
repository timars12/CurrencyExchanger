package com.tim.currencyexchanger.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tim.currencyexchanger.data.model.CurrencyBalance
import com.tim.currencyexchanger.data.model.ExchangeRate
import com.tim.currencyexchanger.data.model.InfoDialogType
import com.tim.currencyexchanger.data.model.Result
import com.tim.currencyexchanger.domain.repository.CurrencyExchangeRepository
import com.tim.currencyexchanger.domain.repository.MyBalanceRepository
import com.tim.currencyexchanger.domain.repository.TransactionResult
import com.tim.currencyexchanger.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import javax.inject.Inject

private const val ENTERED_SELL_AMOUNT_KEY = "enteredSellAmountKey"

data class EnterAmountWithCurrency(val amount: String, val currency: String)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CurrencyExchangeRepository,
    private val balanceRepository: MyBalanceRepository,
    private val useCase: TransactionUseCase,
) : ViewModel() {

    private val _infoDialogType = MutableStateFlow<InfoDialogType>(InfoDialogType.Hide)
    val infoDialogType = _infoDialogType.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _balance = MutableStateFlow(listOf<CurrencyBalance>().toImmutableList())
    val balance = _balance.asStateFlow()

    private val _sell = MutableStateFlow(
        EnterAmountWithCurrency(
            amount = savedStateHandle.get<String>(ENTERED_SELL_AMOUNT_KEY) ?: "", currency = "EUR"
        )
    )
    val sell = _sell.asStateFlow()

    private val _buy = MutableStateFlow(CurrencyBalance(amount = BigDecimal("0.00"), currency = ""))
    val buy = _buy.asStateFlow()

    private val allCurrencies = MutableStateFlow(listOf<ExchangeRate>())
    private var listWithoutSelectedCurrency = emptyList<ExchangeRate>()

    @OptIn(FlowPreview::class)
    val searchCurrencies = searchQuery
        .debounce(300)
        .combine(allCurrencies) { searchQuery, _ ->
            when {
                searchQuery.isNotEmpty() -> listWithoutSelectedCurrency.filter { currency ->
                    currency.currency.contains(searchQuery, ignoreCase = true)
                }

                else -> listWithoutSelectedCurrency
            }.toImmutableList()
        }.stateIn(
            scope = viewModelScope,
            initialValue = persistentListOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    init {
        viewModelScope.launch {
            awaitAll(async { getMyBalance() }, async { getExchangeRates() })
        }
    }

    private suspend fun getExchangeRates() {
        repository.getExchangeRates().collect { result ->
            when (result) {
                is Result.Error -> {
                    // show error
                }

                is Result.Success -> {
                    allCurrencies.value.ifEmpty {
                        _buy.update { balance ->
                            balance.copy(
                                currency = result.data?.rates?.first()?.currency!!
                            )
                        }

                        allCurrencies.value =
                            result.data?.rates?.sortedBy { it.currency } ?: emptyList()
                        removeSelectedCurrency()
                    }
                }
            }
        }
    }

    private suspend fun getMyBalance() {
        balanceRepository.getMyBalance().collect { myBalances ->
            myBalances.ifEmpty {
                balanceRepository.addNewBalance(
                    CurrencyBalance(
                        currency = "EUR",
                        amount = BigDecimal.valueOf(1000.00),
                    )
                )
            }
            _balance.update { myBalances.toImmutableList() }
        }
    }

    fun onEnterSellAmount(value: String) {
        if (value.isNotEmpty() && value.toDoubleOrNull() == null) return

        savedStateHandle[ENTERED_SELL_AMOUNT_KEY] = value
        _sell.update {
            it.copy(
                amount = when {
                    value.isNotEmpty() -> value
                    else -> ""
                }
            )
        }

        _buy.update {
            val sellAmount = when {
                sell.value.amount.isEmpty() -> BigDecimal.ZERO
                else -> sell.value.amount.toBigDecimal()
            }
            it.copy(
                amount = sellAmount.multiply(
                    getCurrentRate(
                        sell.value.currency, buy.value.currency
                    )
                ).formatDecimal()
            )
        }
    }

    fun onChangeSellCurrency(currency: CurrencyBalance) {
        _sell.update { it.copy(currency = currency.currency) }
        _buy.update {
            it.copy(
                amount = sell.value.amount.toBigDecimal().multiply(
                    getCurrentRate(
                        currency.currency, buy.value.currency
                    )
                ).formatDecimal()
            )
        }
    }

    fun onChangeReceiveCurrency(currency: ExchangeRate) {
        _buy.update {
            it.copy(
                currency = currency.currency, amount = sell.value.amount.toBigDecimal().multiply(
                    getCurrentRate(
                        sell.value.currency, currency.currency
                    )
                ).formatDecimal()
            )
        }
        removeSelectedCurrency()
    }

    private fun getCurrentRate(sellCurrency: String, buyCurrency: String): BigDecimal {
        if (allCurrencies.value.isEmpty()) return BigDecimal.ZERO
        val sellRate = allCurrencies.value.first { it.currency == sellCurrency }.rate
        val bayRate = allCurrencies.value.first { it.currency == buyCurrency }.rate
        return bayRate.divide(sellRate, MathContext.DECIMAL64)
    }

    private fun removeSelectedCurrency() {
        listWithoutSelectedCurrency = allCurrencies.value.toMutableList()
            .apply {
                removeAll { data ->
                    data.currency == buy.value.currency || data.currency == sell.value.currency
                }
            }
    }

    fun onClickSubmitExchange() {
        if (sell.value.amount.toDouble() <= 0) return
        viewModelScope.launch {
            val result = useCase(
                balance.value, CurrencyBalance(
                    amount = sell.value.amount.toBigDecimal(), currency = sell.value.currency
                ), buy.value
            )
            _infoDialogType.update {
                when (result) {
                    is TransactionResult.Error -> InfoDialogType.Error(result.message)
                    is TransactionResult.Success -> InfoDialogType.TransactionSuccess(
                        result.transactionFee, sell.value, buy.value
                    )
                }
            }
        }
    }

    fun onClickHideDialog() = _infoDialogType.update { InfoDialogType.Hide }

    private fun BigDecimal.formatDecimal(): BigDecimal = this.setScale(2, RoundingMode.HALF_EVEN)

    fun onSearchQueryChange(query: String) = _searchQuery.update { query }
}

