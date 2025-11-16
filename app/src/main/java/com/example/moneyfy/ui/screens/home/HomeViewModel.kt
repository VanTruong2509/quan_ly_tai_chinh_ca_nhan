package com.example.moneyfy.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import com.example.moneyfy.data.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Spending(val amount: Float)

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStore = DataStoreManager(app)

    // Tổng tiền
    private val _totalMoney = MutableStateFlow(0f)
    val totalMoney: StateFlow<Float> = _totalMoney

    // Danh sách chi tiêu
    private val _spendings = mutableStateListOf<Spending>()
    val spendings: List<Spending> get() = _spendings

    init {
        // Load tổng tiền từ DataStore
        viewModelScope.launch {
            dataStore.totalMoney.collect { value ->
                _totalMoney.value = value
            }
        }
    }

    fun addSpending(amount: Float) {
        if (amount <= 0f) return
        _spendings.add(Spending(amount))
        val newTotal = _totalMoney.value - amount
        _totalMoney.value = newTotal

        viewModelScope.launch {
            dataStore.saveTotalMoney(newTotal)
        }
    }

    // Lấy 7 chi tiêu gần nhất để vẽ biểu đồ tuần
    fun getWeeklySpending(): List<Spending> {
        return _spendings.takeLast(7)
    }
}
