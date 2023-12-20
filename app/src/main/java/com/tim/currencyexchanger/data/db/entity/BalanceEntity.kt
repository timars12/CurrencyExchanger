package com.tim.currencyexchanger.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tim.currencyexchanger.data.model.CurrencyBalance
import java.math.BigDecimal

@Entity
data class BalanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: BigDecimal,
    val currency: String,
) {
    fun convertToCurrencyBalance(): CurrencyBalance =
        CurrencyBalance(currency = currency, amount = amount)
}