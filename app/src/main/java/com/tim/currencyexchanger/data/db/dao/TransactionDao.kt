package com.tim.currencyexchanger.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tim.currencyexchanger.data.db.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionToDB(transaction: TransactionEntity): Long

    @Query("select COUNT(id) from transaction_entity")
    suspend fun getCountOfAllTransaction(): Int
}