package com.tim.currencyexchanger.ui.auth

import androidx.lifecycle.ViewModel
import com.tim.currencyexchanger.utils.NavigationDispatcher
import com.tim.currencyexchanger.utils.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
) : ViewModel(), MviViewModel<SignInViewIntent, SignInViewState> {

    private val _state: MutableStateFlow<SignInViewState> =
        MutableStateFlow(SignInViewState.initial())
    override val viewState: StateFlow<SignInViewState> get() = _state

    fun sendEvent(event: SignInViewIntent) {
        when (event) {
            SignInViewIntent.EmailSignIn -> {
                navigationDispatcher.emit {
                    it.navigate("currencyExchanger/11/22?timId=33&setNumber=44")
                }
            }

            is SignInViewIntent.EnterEmail -> {
                _state.value =
                    _state.value.copy(email = _state.value.email.copy(data = event.email))
            }

            is SignInViewIntent.EnterPassword -> {
                _state.value =
                    _state.value.copy(password = _state.value.password.copy(data = event.password))
            }

            SignInViewIntent.ForgotPassword -> TODO()
        }
    }
}
