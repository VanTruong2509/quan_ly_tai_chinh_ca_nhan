package com.example.moneyfy.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyfy.data.AppDatabase
import com.example.moneyfy.data.DataStoreManager
import com.example.moneyfy.data.model.Spending
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStore = DataStoreManager(app)
    private val db = AppDatabase.getDatabase(app)
    private val spendingDao = db.spendingDao()

    // Tổng tiền
    private val _totalMoney = MutableStateFlow(0f)
    val totalMoney: StateFlow<Float> = _totalMoney

    // Danh sách chi tiêu/thu nhập
    private val _spendings = MutableStateFlow<List<Spending>>(emptyList())
    val spendings: StateFlow<List<Spending>> = _spendings

    init {
        // Load tổng tiền từ DataStore
        viewModelScope.launch {
            dataStore.totalMoney.collect { value ->
                _totalMoney.value = value
            }
        }

        // Load dữ liệu từ Room
        viewModelScope.launch {
            _spendings.value = spendingDao.getAllSpendings()
        }
    }

    // Thêm chi tiêu
    fun addSpending(amount: Float, category: String = "Khác", note: String = "") {
        if (amount <= 0f) return

        viewModelScope.launch {
            val newSpending = Spending(
                amount = amount,
                category = category,
                note = note
            )

            // Lưu vào database
            spendingDao.insertSpending(newSpending)

            // Load lại danh sách
            _spendings.value = spendingDao.getAllSpendings()

            // Cập nhật tổng tiền
            val newTotal = _totalMoney.value - amount
            _totalMoney.value = newTotal
            dataStore.saveTotalMoney(newTotal)
        }
    }

    // Thêm thu nhập
    fun addIncome(amount: Float, category: String = "Thu nhập", note: String = "") {
        if (amount <= 0f) return

        viewModelScope.launch {
            val newSpending = Spending(
                amount = -amount,   // âm = thu nhập
                category = category,
                note = note
            )

            spendingDao.insertSpending(newSpending)

            _spendings.value = spendingDao.getAllSpendings()

            val newTotal = _totalMoney.value + amount
            _totalMoney.value = newTotal
            dataStore.saveTotalMoney(newTotal)
        }
    }

    // Lấy 7 giao dịch gần đây
    fun getWeeklySpending(): List<Spending> {
        return _spendings.value.takeLast(7)
    }
}
