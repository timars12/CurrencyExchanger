package com.tim.currencyexchanger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tim.currencyexchanger.R
import com.tim.currencyexchanger.data.model.CurrencyBottomSheetState
import com.tim.currencyexchanger.data.model.InfoDialogType
import com.tim.currencyexchanger.ui.composable.ChangeBuyCurrencyBottomSheet
import com.tim.currencyexchanger.ui.composable.ChangeSellCurrencyBottomSheet
import com.tim.currencyexchanger.ui.composable.CurrencyExchange
import com.tim.currencyexchanger.ui.composable.MyBalance
import com.tim.currencyexchanger.ui.composable.OnClick
import com.tim.currencyexchanger.ui.composable.ShowAlertDialog
import com.tim.currencyexchanger.ui.composable.SubmitButton
import com.tim.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.tim.currencyexchanger.ui.theme.ToolbarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyExchangerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyExchangerScreen()
                }
            }
        }
    }
}

@Composable
fun CurrencyExchangerScreen(viewModel: MainViewModel = viewModel()) {
    val modifier = Modifier.fillMaxWidth()
    var showSheet by remember { mutableStateOf<CurrencyBottomSheetState>(CurrencyBottomSheetState.Hide) }
    val infoDialogType by viewModel.infoDialogType.collectAsStateWithLifecycle()
    val balance by viewModel.balance.collectAsStateWithLifecycle()
    val sell by viewModel.sell.collectAsStateWithLifecycle()
    val receive by viewModel.buy.collectAsStateWithLifecycle()
    val allCurrencies by viewModel.searchCurrencies.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    when (showSheet) {
        CurrencyBottomSheetState.ChangeReceiveCurrency -> {
            ChangeBuyCurrencyBottomSheet(
                allCurrencies = allCurrencies,
                searchQuery = searchQuery,
                onCurrencySelected = remember { viewModel::onChangeReceiveCurrency },
                onSearchQueryChange = remember { viewModel::onSearchQueryChange }
            ) {
                showSheet = CurrencyBottomSheetState.Hide
            }
        }

        CurrencyBottomSheetState.ChangeSellCurrency -> ChangeSellCurrencyBottomSheet(
            balance,
            onCurrencySelected = remember { viewModel::onChangeSellCurrency }
        ) {
            showSheet = CurrencyBottomSheetState.Hide
        }

        CurrencyBottomSheetState.Hide -> Unit
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = modifier
                .background(color = ToolbarColor)
                .padding(top = 24.dp, bottom = 16.dp),
            text = stringResource(R.string.title),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        MyBalance(
            modifier = modifier.padding(start = 16.dp),
            balance = balance
        )
        Spacer(modifier = Modifier.height(16.dp))
        CurrencyExchange(
            modifier = modifier.padding(start = 16.dp),
            sellAmount = sell.amount,
            sellCurrency = sell.currency,
            onEnterSellAmount = remember { viewModel::onEnterSellAmount },
            onChangeSellCurrency = { showSheet = CurrencyBottomSheetState.ChangeSellCurrency },
            receiveAmount = receive.amount.toString(),
            receiveCurrency = receive.currency,
            onChangeReceiveCurrency = {
                showSheet = CurrencyBottomSheetState.ChangeReceiveCurrency
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        SubmitButton(onClick = remember { viewModel::onClickSubmitExchange })

        InfoDialogHandler(infoDialogType, remember { viewModel::onClickHideDialog })
    }
}

@Composable
private fun InfoDialogHandler(
    infoDialogType: InfoDialogType,
    onClickHideDialog: OnClick,
) {
    when (infoDialogType) {
        is InfoDialogType.Hide -> Unit
        is InfoDialogType.Error -> ShowAlertDialog(
            infoDialogType.text ?: "",
            onClickHideDialog
        )

        is InfoDialogType.TransactionSuccess -> ShowAlertDialog(
            text = stringResource(
                R.string.you_have_converted_to_commission_fee,
                infoDialogType.sell.amount,
                infoDialogType.sell.currency,
                infoDialogType.buy.amount,
                infoDialogType.buy.currency,
                infoDialogType.fee,
                infoDialogType.sell.currency
            ),
            onClickHideDialog
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyExchangerTheme {
        CurrencyExchangerScreen()
    }
}