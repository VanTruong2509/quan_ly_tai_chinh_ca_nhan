package com.example.moneyfy.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import com.example.moneyfy.data.DataStoreManager
import com.example.moneyfy.data.model.Spending
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStore = DataStoreManager(app)

    private val _totalMoney = MutableStateFlow(0f)
    val totalMoney: StateFlow<Float> = _totalMoney

    private val _spendings = mutableStateListOf<Spending>()
    val spendings: List<Spending> get() = _spendings

    init {
        viewModelScope.launch {
            dataStore.totalMoney.collect { value ->
                _totalMoney.value = value
            }
        }
    }

    // Chi tiêu
    fun addSpending(amount: Float, category: String = "Khác", note: String = "") {
        if (amount <= 0f) return
        _spendings.add(Spending(amount, category, note))
        val newTotal = _totalMoney.value - amount
        _totalMoney.value = newTotal

        viewModelScope.launch { dataStore.saveTotalMoney(newTotal) }
    }

    // Thu nhập
    fun addIncome(amount: Float, category: String = "Thu nhập", note: String = "") {
        if (amount <= 0f) return
        _spendings.add(Spending(-amount, category, note)) // negative để phân biệt thu nhập
        val newTotal = _totalMoney.value + amount
        _totalMoney.value = newTotal

        viewModelScope.launch { dataStore.saveTotalMoney(newTotal) }
    }

    fun getWeeklySpending(): List<Spending> {
        return _spendings.takeLast(7)
    }
}
