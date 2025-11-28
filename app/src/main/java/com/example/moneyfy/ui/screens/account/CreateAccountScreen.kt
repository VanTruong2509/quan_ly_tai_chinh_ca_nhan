package com.example.moneyfy.ui.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyfy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(onBack: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tạo Tài Khoản", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color(0xFF101010)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AccountTypeCard(
                title = "Đồng bộ ngân hàng",
                desc = "Kết nối với tài khoản ngân hàng của bạn\nTự động đồng bộ giao dịch vào Wallet.",
                icon = R.drawable.ic_bank,   // ⭐ icon ngân hàng
                onClick = {}
            )

            AccountTypeCard(
                title = "Đầu tư",
                desc = "Theo dõi cổ phiếu, ETF của bạn và\nnhận giá trị đầu tư cập nhật tự động.",
                icon = R.drawable.ic_invest,  // ⭐ icon biểu đồ
                onClick = {}
            )
        }
    }
}

@Composable
fun AccountTypeCard(
    title: String,
    desc: String,
    icon: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF007BFF)),
        shape = MaterialTheme.shapes.medium
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(desc, fontSize = 14.sp, color = Color.White)
            }

            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
