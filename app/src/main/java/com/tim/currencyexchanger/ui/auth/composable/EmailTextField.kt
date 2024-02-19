package com.tim.currencyexchanger.ui.auth.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tim.currencyexchanger.ui.auth.FieldText
import com.tim.currencyexchanger.utils.OnEnterText
import com.tim.currencyexchanger.utils.mvi.ErrorType
import com.tim.currencyexchanger.utils.mvi.MviError

@Composable
internal fun EmailTextField(
    data: FieldText,
    focusRequester: FocusRequester,
    onEnterText: OnEnterText
) {
    TextFieldWithError(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        data = data,
        hint = "Email",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() }),
        onEnterText = onEnterText
    )
}

@Preview
@Composable
fun TestEmailTextField() {
    EmailTextField(
        FieldText("tim@df", MviError(type = ErrorType.FIELD, "Something wrong")),
        FocusRequester()
    ) {}
}