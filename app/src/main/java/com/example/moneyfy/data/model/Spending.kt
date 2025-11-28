package com.example.moneyfy.data.model

data class Spending(
    val amount: Float,
    val category: String = "Khác",
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
