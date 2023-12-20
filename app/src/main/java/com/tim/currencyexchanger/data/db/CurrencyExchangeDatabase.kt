package com.tim.currencyexchanger.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tim.currencyexchanger.data.db.converters.BigDecimalTypeConverter
import com.tim.currencyexchanger.data.db.dao.BalanceDao
import com.tim.currencyexchanger.data.db.dao.TransactionDao
import com.tim.currencyexchanger.data.db.entity.BalanceEntity
import com.tim.currencyexchanger.data.db.entity.TransactionEntity
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

private const val APP_DATABASE = "timApp.db"

@Database(
    entities = [
        TransactionEntity::class,
        BalanceEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class CurrencyExchangeDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun balanceDao(): BalanceDao

    companion object {
        @Volatile
        private var INSTANCE: CurrencyExchangeDatabase? = null

        fun getInstance(context: Context): CurrencyExchangeDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context): CurrencyExchangeDatabase {
            return Room.databaseBuilder(
                context,
                CurrencyExchangeDatabase::class.java,
                APP_DATABASE
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}