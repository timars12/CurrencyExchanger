package com.tim.currencyexchanger.data.model

sealed interface Result<T> {
    data class Success<T>(val data: T?) : Result<T>
    data class Error<T>(val exception: Exception) : Result<T>
}