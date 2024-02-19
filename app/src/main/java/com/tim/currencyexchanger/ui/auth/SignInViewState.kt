package com.tim.currencyexchanger.ui.auth

import android.os.Parcelable
import androidx.annotation.StringRes
import com.tim.currencyexchanger.utils.mvi.MviError
import com.tim.currencyexchanger.utils.mvi.MviViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInViewState(
    val email: FieldText = FieldText(data = null, error = null),
    val password: FieldText = FieldText(data = null, error = null),
    val isLoading: Boolean,
    val isLoginBtnEnable: Boolean,
    val isLoginSuccess: Boolean = false,
    override val error: MviError?
) : MviViewState, Parcelable {
    companion object {
        fun initial() =
            SignInViewState(
                isLoading = false,
                isLoginBtnEnable = true,
                error = null
            )
    }
}

@Parcelize
data class FieldText(val data: String?, val error: MviError? = null) : Parcelable

