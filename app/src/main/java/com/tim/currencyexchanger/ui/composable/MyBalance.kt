package com.tim.currencyexchanger.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tim.currencyexchanger.R
import com.tim.currencyexchanger.data.model.CurrencyBalance
import com.tim.currencyexchanger.ui.theme.CurrencyExchangerTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun MyBalance(
    modifier: Modifier,
    balance: ImmutableList<CurrencyBalance>,
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.my_balances).uppercase(),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(36.dp),
    ) {
        items(balance, key = CurrencyBalance::currency) { balance ->
            Text(
                text = "${formatDecimal(balance.amount)} ${balance.currency}",
            )
        }
    }
}

private fun formatDecimal(decimal: BigDecimal): String =
    decimal.setScale(2, RoundingMode.HALF_EVEN).toPlainString()

@Preview(showBackground = true)
@Composable
fun MyBalancePreview() {
    CurrencyExchangerTheme {
        Column {
            MyBalance(
                modifier = Modifier.fillMaxWidth(),
                balance = persistentListOf(
                    CurrencyBalance(amount = BigDecimal(100), currency = "USD"),
                    CurrencyBalance(amount = BigDecimal(200), currency = "EUR")
                )
            )
        }
    }
}