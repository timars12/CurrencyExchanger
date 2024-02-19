package com.tim.currencyexchanger.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tim.currencyexchanger.ui.auth.composable.EmailTextField
import com.tim.currencyexchanger.ui.auth.composable.PasswordTextField
import com.tim.currencyexchanger.ui.auth.composable.SnackbarMessage
import com.tim.currencyexchanger.ui.theme.CurrencyExchangerTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel()
) {
    val focusRequester = remember { FocusRequester() }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val dispatch = remember {
        { intent: SignInViewIntent -> viewModel.sendEvent(intent) }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailTextField(
                viewState.email,
                focusRequester,
                onEnterText = { email: String -> dispatch(SignInViewIntent.EnterEmail(email)) }
            )
            PasswordTextField(
                data = viewState.password,
                focusRequester = focusRequester,
                onEnterText = { password: String ->
                    dispatch(SignInViewIntent.EnterPassword(password))
                },
                onClickDone = { dispatch(SignInViewIntent.EmailSignIn) },
                keyboardController = keyboardController
            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = viewState.isLoginBtnEnable,
                onClick = {
                    keyboardController?.hide()
                    dispatch(SignInViewIntent.EmailSignIn)
                }
            ) {
                if (viewState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        trackColor = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Text("sign in")
                }
            }
        }
        SnackbarMessage(
            snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyExchangerTheme {
        SignInScreen()
    }
}