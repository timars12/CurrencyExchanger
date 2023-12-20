package com.tim.currencyexchanger.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tim.currencyexchanger.data.db.entity.BalanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalanceToDB(balance: BalanceEntity)

    @Update
    suspend fun updateBalance(balance: BalanceEntity)

    @Query("select * from balanceentity")
    fun getBalance(): Flow<List<BalanceEntity>>


    @Query("select * from balanceentity where currency = :currency")
    suspend fun getBalanceByCurrency(currency: String): BalanceEntity?
}