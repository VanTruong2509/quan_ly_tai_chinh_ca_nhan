package com.example.moneyfy.ui.screens.more

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyfy.R
import androidx.compose.foundation.shape.RoundedCornerShape


// ======================= DATABASE =======================

class MoreDatabase(context: Context) :
    SQLiteOpenHelper(context, "more.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE notes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                content TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                category TEXT,
                amount TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertNote(content: String) {
        writableDatabase.execSQL(
            "INSERT INTO notes(content) VALUES(?)",
            arrayOf(content)
        )
    }

    fun deleteNote(content: String) {
        writableDatabase.execSQL(
            "DELETE FROM notes WHERE content = ?",
            arrayOf(content)
        )
    }

    fun insertTransaction(category: String, amount: String) {
        writableDatabase.execSQL(
            "INSERT INTO transactions(category, amount) VALUES(?,?)",
            arrayOf(category, amount)
        )
    }

    fun deleteTransaction(category: String, amount: String) {
        writableDatabase.execSQL(
            "DELETE FROM transactions WHERE category = ? AND amount = ?",
            arrayOf(category, amount)
        )
    }
}

// ======================= MAIN MORE SCREEN =======================

@Composable
fun MoreScreen(
    onSettingsClick: () -> Unit,
    onInvestmentClick: () -> Unit,
    onPlanningClick: () -> Unit
) {
    var currentScreen by remember { mutableStateOf("") }

    when (currentScreen) {
        "" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF000000)) // đen sâu đẹp hơn
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Tiện ích",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                val items = listOf(
                    MoreItem("Ghi chú", R.drawable.ic_note),
                    MoreItem("Lập kế hoạch", R.drawable.ic_plan),
                    MoreItem("Đầu tư", R.drawable.ic_chart),
                    MoreItem("Theo dõi chi tiêu", R.drawable.ic_heart),
                    MoreItem("Cài đặt", R.drawable.ic_settings),
                    MoreItem("Trợ giúp", R.drawable.ic_help)
                )

                for (row in items.chunked(2)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { item ->
                            FeatureCard(
                                title = item.title,
                                iconRes = item.icon,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(125.dp),
                                onClick = {
                                    when (item.title) {
                                        "Cài đặt" -> onSettingsClick()
                                        "Đầu tư" -> onInvestmentClick()
                                        "Lập kế hoạch" -> onPlanningClick()
                                        "Trợ giúp" -> currentScreen = "help"
                                        "Ghi chú" -> currentScreen = "notes"
                                        "Theo dõi chi tiêu" -> currentScreen = "tracking"
                                    }
                                }
                            )
                        }
                        if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        "help" -> HelpScreen { currentScreen = "" }
        "notes" -> NotesScreen { currentScreen = "" }
        "tracking" -> TrackingScreen { currentScreen = "" }
    }
}

// ======================= DATA MODEL =======================

data class MoreItem(val title: String, val icon: Int)

// ======================= CARD UI =======================

@Composable
fun FeatureCard(
    title: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable { onClick() }, // ❌ bỏ padding ngoài Card để tránh bó layout
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E3A8A)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), // ✅ chỉ giữ padding bên trong
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // ✅ đẩy đều icon + chữ
        ) {

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(
                        Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,                 // ✅ không cho rớt xuống nửa chữ
                softWrap = false,             // ✅ không vỡ chữ
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
    }
}

// ======================= HELP =======================

@Composable
fun HelpScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ) {
        Text("Hướng dẫn", color = Color.White, fontSize = 24.sp)

        Spacer(Modifier.height(14.dp))

        Text(
            """
• Thêm giao dịch chi tiêu
• Theo dõi tài chính cá nhân
• Ghi chú mục tiêu tiết kiệm
• Lập kế hoạch ngân sách
            """.trimIndent(),
            color = Color.Gray
        )

        Spacer(Modifier.height(20.dp))

        Text("⬅ Quay lại", color = Color.Green, modifier = Modifier.clickable { onBack() })
    }
}

// ======================= NOTES - THÊM + XOÁ =======================

@Composable
fun NotesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = remember { MoreDatabase(context) }

    var input by remember { mutableStateOf("") }
    val notes = remember { mutableStateListOf<String>() }

    fun loadNotes() {
        notes.clear()
        val cursor = db.readableDatabase.rawQuery("SELECT content FROM notes", null)
        while (cursor.moveToNext()) notes.add(cursor.getString(0))
        cursor.close()
    }

    LaunchedEffect(Unit) { loadNotes() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        Text("📒 Ghi chú", color = Color.White, fontSize = 22.sp)

        Spacer(Modifier.height(10.dp))

        TextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Nhập ghi chú...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (input.isNotBlank()) {
                    db.insertNote(input)
                    input = ""
                    loadNotes()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Lưu") }

        Spacer(Modifier.height(16.dp))

        notes.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(it, color = Color.White)

                    Text(
                        "❌",
                        color = Color.Red,
                        modifier = Modifier.clickable {
                            db.deleteNote(it)
                            loadNotes()
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text("⬅ Quay lại", color = Color.Green, modifier = Modifier.clickable { onBack() })
    }
}

// ======================= TRACKING - THÊM + XOÁ =======================

@Composable
fun TrackingScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = remember { MoreDatabase(context) }

    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val transactions = remember { mutableStateListOf<Pair<String, String>>() }

    fun loadData() {
        transactions.clear()
        val cursor = db.readableDatabase.rawQuery("SELECT category, amount FROM transactions", null)
        while (cursor.moveToNext()) {
            transactions.add(cursor.getString(0) to cursor.getString(1))
        }
        cursor.close()
    }

    LaunchedEffect(Unit) { loadData() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        Text("📊 Theo dõi chi tiêu", color = Color.White, fontSize = 22.sp)

        Spacer(Modifier.height(10.dp))

        TextField(
            value = category,
            onValueChange = { category = it },
            placeholder = { Text("Danh mục") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        TextField(
            value = amount,
            onValueChange = { amount = it },
            placeholder = { Text("Số tiền") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (category.isNotBlank() && amount.isNotBlank()) {
                    db.insertTransaction(category, amount)
                    category = ""
                    amount = ""
                    loadData()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lưu giao dịch")
        }

        Spacer(Modifier.height(16.dp))

        transactions.forEach { (cat, money) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(cat, color = Color.White)
                    Text(money, color = Color.Green)

                    Text(
                        " ❌",
                        color = Color.Red,
                        modifier = Modifier.clickable {
                            db.deleteTransaction(cat, money)
                            loadData()
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text("⬅ Quay lại", color = Color.Green, modifier = Modifier.clickable { onBack() })
    }
}
