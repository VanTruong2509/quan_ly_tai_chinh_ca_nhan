package com.example.moneyfy.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SpendingDao {

    @Insert
    suspend fun insertSpending(spending: Spending)

    @Query("SELECT * FROM spendings ORDER BY timestamp DESC")
    suspend fun getAllSpendings(): List<Spending>

    @Query("SELECT * FROM spendings ORDER BY timestamp DESC LIMIT 7")
    suspend fun getLatest7(): List<Spending>
}
