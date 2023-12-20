package com.tim.currencyexchanger.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tim.currencyexchanger.R
import com.tim.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.tim.currencyexchanger.ui.theme.DarkGreen

typealias OnEnterAmount = (String) -> Unit
typealias OnClick = () -> Unit

@Composable
fun CurrencyExchange(
    modifier: Modifier,
    sellAmount: String,
    sellCurrency: String,
    onEnterSellAmount: OnEnterAmount,
    onChangeSellCurrency: OnClick,
    receiveAmount: String,
    receiveCurrency: String,
    onChangeReceiveCurrency: OnClick,
) {
    Column(modifier = modifier.padding(end = 16.dp)) {
        Text(
            text = stringResource(R.string.my_balances).uppercase(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconStatus(
                color = Red,
                icon = android.R.drawable.arrow_up_float,
                contentDescription = stringResource(id = R.string.sell)
            )
            Text(
                text = stringResource(R.string.sell),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            TextField(
                modifier = Modifier.width(110.dp),
                value = sellAmount,
                onValueChange = onEnterSellAmount,
                textStyle = TextStyle(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                visualTransformation = remember { AmountVisualTransformation() },
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "0.00",
                        textAlign = TextAlign.End
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            CurrencySection(
                text = sellCurrency,
                contentDescription = stringResource(id = R.string.sell),
                onClick = onChangeSellCurrency
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(Modifier.padding(start = 52.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconStatus(
                color = DarkGreen,
                icon = android.R.drawable.arrow_down_float,
                contentDescription = stringResource(id = R.string.receive)
            )
            Text(
                text = stringResource(R.string.receive),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                modifier = Modifier.width(110.dp).padding(end = 16.dp),
                textAlign = TextAlign.End,
                text = "+$receiveAmount",
                maxLines = 1,
                color = DarkGreen
            )
            CurrencySection(
                text = receiveCurrency,
                contentDescription = stringResource(id = R.string.receive),
                onClick = onChangeReceiveCurrency
            )
        }
    }
}

@Composable
private fun IconStatus(
    color: Color,
    @DrawableRes icon: Int,
    contentDescription: String?,
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(color, CircleShape),
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun CurrencySection(
    text: String,
    contentDescription: String?,
    onClick: OnClick,
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = text)
        Icon(
            painter = painterResource(id = android.R.drawable.arrow_down_float),
            contentDescription = contentDescription,
            tint = Color.Black,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyExchangePreview() {
    CurrencyExchangerTheme {
        CurrencyExchange(
            modifier = Modifier.fillMaxSize(),
            sellAmount = "100",
            sellCurrency = "EUR",
            onEnterSellAmount = {},
            onChangeSellCurrency = {},
            receiveAmount = "111",
            receiveCurrency = "USD",
            onChangeReceiveCurrency = {}
        )
    }
}