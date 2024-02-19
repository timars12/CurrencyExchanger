package com.tim.currencyexchanger.ui.auth

import com.tim.currencyexchanger.utils.mvi.MviIntent

sealed interface SignInViewIntent : MviIntent {
    object ForgotPassword : SignInViewIntent
    data class EnterEmail(val email: String?) : SignInViewIntent
    data class EnterPassword(val password: String?) : SignInViewIntent
    object EmailSignIn : SignInViewIntent
}

