package com.example.moneyfy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spendings")
data class Spending(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Float,
    val category: String = "Khác",
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
