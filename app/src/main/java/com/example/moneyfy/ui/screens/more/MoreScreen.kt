package com.example.moneyfy.ui.screens.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
                            }
                        }
                    )
                }

                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

data class MoreItem(
    val title: String,
    val icon: Int
)

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
