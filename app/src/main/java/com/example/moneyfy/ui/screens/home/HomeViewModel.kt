package com.example.moneyfy.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyfy.data.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf

data class Spending(val amount: Float)

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

    fun addSpending(amount: Float) {
        _spendings.add(Spending(amount))
        val newTotal = _totalMoney.value - amount
        _totalMoney.value = newTotal
        viewModelScope.launch {
            dataStore.saveTotalMoney(newTotal)
        }
    }
}
