package com.example.moneyfy.data

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Khởi tạo DataStore
val Context.dataStore by preferencesDataStore(name = "moneyfy_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val TOTAL_MONEY = floatPreferencesKey("total_money")
    }

    // 🧾 Lấy dữ liệu (Flow)
    val totalMoney: Flow<Float> = context.dataStore.data
        .map { prefs ->
            prefs[TOTAL_MONEY] ?: 0f
        }

    // 💾 Lưu dữ liệu
    suspend fun saveTotalMoney(value: Float) {
        context.dataStore.edit { prefs ->
            prefs[TOTAL_MONEY] = value
        }
    }
}
