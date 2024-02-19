package com.tim.currencyexchanger.ui.currencyExchang.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tim.currencyexchanger.R

@Composable
fun ShowAlertDialog(
    text: String,
    onClickClose: OnClick,
) {
    AlertDialog(
        onDismissRequest = onClickClose,
        title = { Text(text = stringResource(R.string.currency_converted)) },
        text = { Text(text = text) },
        confirmButton = {
            Button(onClick = onClickClose) {
                Text(stringResource(R.string.done))
            }
        },
    )
}