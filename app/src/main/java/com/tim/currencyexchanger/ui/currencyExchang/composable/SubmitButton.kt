package com.tim.currencyexchanger.ui.currencyExchang.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tim.currencyexchanger.R
import com.tim.currencyexchanger.ui.theme.ToolbarColor

@Composable
fun SubmitButton(onClick: OnClick) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ToolbarColor,
        )
    ) {
        Text(
            text = stringResource(R.string.submit).uppercase(),
            color = Color.White,
        )
    }
}