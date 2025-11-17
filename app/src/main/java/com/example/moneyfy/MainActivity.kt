package com.example.moneyfy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.moneyfy.ui.navigation.AppNavHost
import com.example.moneyfy.ui.theme.MoneyfyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyfyTheme {
                // ✅ Gọi AppNavHost() là đủ – không cần nhớ NavController ở đây
                AppNavHost()
            }
        }
    }
}
