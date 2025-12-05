package com.example.moneyfy.ui.screens.notification

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ======================= SQLITE DATABASE =======================

data class NotificationItem(
    val id: Int,
    val title: String,
    val content: String
)

class NotificationDatabase(context: Context) :
    SQLiteOpenHelper(context, "notification.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE notifications (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                content TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertNotification(title: String, content: String) {
        writableDatabase.execSQL(
            "INSERT INTO notifications(title, content) VALUES(?,?)",
            arrayOf(title, content)
        )
    }

    fun deleteNotification(id: Int) {
        writableDatabase.execSQL(
            "DELETE FROM notifications WHERE id = ?",
            arrayOf(id)
        )
    }

    fun getAllNotifications(): List<NotificationItem> {
        val list = mutableListOf<NotificationItem>()
        val cursor = readableDatabase.rawQuery(
            "SELECT id, title, content FROM notifications ORDER BY id DESC",
            null
        )

        while (cursor.moveToNext()) {
            list.add(
                NotificationItem(
                    id = cursor.getInt(0),
                    title = cursor.getString(1),
                    content = cursor.getString(2)
                )
            )
        }
        cursor.close()
        return list
    }
    fun getNotificationCount(): Int {
        val cursor = readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM notifications",
            null
        )
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

}

// ✅ HÀM GỌI LƯU THÔNG BÁO TỪ MÀN KHÁC (VD: KHI CHI TIỀN)
fun addNotificationSQL(
    context: Context,
    title: String,
    content: String
) {
    val db = NotificationDatabase(context)
    db.insertNotification(title, content)
}

// ======================= UI SCREEN =======================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { NotificationDatabase(context) }
    val notifications = remember { mutableStateListOf<NotificationItem>() }

    fun loadData() {
        notifications.clear()
        notifications.addAll(db.getAllNotifications())
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thông báo",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFF101010)
    ) { padding ->

        if (notifications.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bạn không có thông báo nào.",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications, key = { it.id }) { notification ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = notification.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = notification.content,
                                    color = Color.LightGray,
                                    fontSize = 14.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Xóa",
                                tint = Color.Red,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        db.deleteNotification(notification.id)
                                        loadData()
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}
