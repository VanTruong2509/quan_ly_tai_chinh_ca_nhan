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

// --- Thông báo giả lập ---
data class NotificationItem(val id: Int, val title: String, val content: String)

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

    // --- Thông báo ---
    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications
    private var nextNotificationId = 1

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

        // Thêm thông báo giả lập mặc định (demo)
        _notifications.value = listOf(
            NotificationItem(nextNotificationId++, "Giao dịch thành công", "Bạn đã chi 200.000 VNĐ cho mua sắm."),
            NotificationItem(nextNotificationId++, "Nhắc nhở", "Đặt mục tiêu tiết kiệm tháng này."),
            NotificationItem(nextNotificationId++, "Đầu tư", "Cổ phiếu ABC đã tăng 5%.")
        )
    }

    // Thêm chi tiêu
    fun addSpending(amount: Float, category: String = "Khác", note: String = "") {
        if (amount <= 0f) return

        viewModelScope.launch {
            val newSpending = Spending(amount = amount, category = category, note = note)
            spendingDao.insertSpending(newSpending)
            _spendings.value = spendingDao.getAllSpendings()

            val newTotal = _totalMoney.value - amount
            _totalMoney.value = newTotal
            dataStore.saveTotalMoney(newTotal)

            // Thêm thông báo giả lập
            addNotification("Chi tiêu", "Bạn đã chi $amount VNĐ cho $category.")
        }
    }

    // Thêm thu nhập
    fun addIncome(amount: Float, category: String = "Thu nhập", note: String = "") {
        if (amount <= 0f) return

        viewModelScope.launch {
            val newIncome = Spending(amount = -amount, category = category, note = note)
            spendingDao.insertSpending(newIncome)
            _spendings.value = spendingDao.getAllSpendings()

            val newTotal = _totalMoney.value + amount
            _totalMoney.value = newTotal
            dataStore.saveTotalMoney(newTotal)

            // Thêm thông báo giả lập
            addNotification("Thu nhập", "Bạn đã nhận $amount VNĐ từ $category.")
        }
    }

    // Lấy 7 giao dịch gần đây
    fun getWeeklySpending(): List<Spending> = _spendings.value.takeLast(7)

    // --- Thông báo ---
    private fun addNotification(title: String, content: String) {
        val newNotification = NotificationItem(nextNotificationId++, title, content)
        _notifications.value = listOf(newNotification) + _notifications.value
    }

    fun removeNotification(id: Int) {
        _notifications.value = _notifications.value.filter { it.id != id }
    }
}
