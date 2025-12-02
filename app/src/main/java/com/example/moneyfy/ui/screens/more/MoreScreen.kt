package com.example.moneyfy.ui.screens.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyfy.R

@Composable
fun MoreScreen(
    onSettingsClick: () -> Unit,
    onInvestmentClick: () -> Unit,
    onPlanningClick: () -> Unit
) {
    var currentScreen by remember { mutableStateOf("") }

    when (currentScreen) {
        "" -> {
            // Màn hình chính MoreScreen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF101010))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Thêm",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                val items = listOf(
                    MoreItem("Bản ghi chép", R.drawable.ic_note),
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
                                    .height(120.dp),
                                onClick = {
                                    when (item.title) {
                                        "Cài đặt" -> onSettingsClick()
                                        "Đầu tư" -> onInvestmentClick()
                                        "Lập kế hoạch" -> onPlanningClick()
                                        "Trợ giúp" -> currentScreen = "help"
                                        "Bản ghi chép" -> currentScreen = "notes"
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

        "help" -> HelpScreen(onBack = { currentScreen = "" })
        "notes" -> NotesScreen(onBack = { currentScreen = "" })
        "tracking" -> TrackingScreen(onBack = { currentScreen = "" })
    }
}

data class MoreItem(val title: String, val icon: Int)

@Composable
fun FeatureCard(
    title: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }
    }
}

// --- HelpScreen ---
@Composable
fun HelpScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        Text(
            text = "Hướng dẫn sử dụng",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = """
                1. Thêm giao dịch: Chọn "Thêm" ở thanh dưới cùng.
                2. Xem thống kê: Chọn "Thống kê".
                3. Quản lý tài khoản: Vào "Cài đặt".
                4. Lập kế hoạch chi tiêu: Chọn "Lập kế hoạch".
                5. Theo dõi đầu tư: Chọn "Đầu tư".
            """.trimIndent(),
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quay lại",
            color = Color.Green,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable { onBack() }
                .padding(8.dp)
        )
    }
}

// --- NotesScreen ---
@Composable
fun NotesScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        Text(
            text = "Bản ghi chép",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Danh sách các ghi chú và ý tưởng của bạn sẽ hiển thị ở đây.",
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quay lại",
            color = Color.Green,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable { onBack() }
                .padding(8.dp)
        )
    }
}

// --- TrackingScreen ---
@Composable
fun TrackingScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp)
    ) {
        Text(
            text = "Theo dõi chi tiêu",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Ở đây bạn có thể xem các giao dịch chi tiêu và quản lý ngân sách của mình.",
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quay lại",
            color = Color.Green,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable { onBack() }
                .padding(8.dp)
        )
    }
}
