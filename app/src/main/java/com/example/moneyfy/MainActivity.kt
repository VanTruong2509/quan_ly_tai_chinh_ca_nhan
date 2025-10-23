package com.example.moneyfy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moneyfy.ui.screens.home.HomeScreen
import com.example.moneyfy.ui.screens.login.*
import com.example.moneyfy.ui.theme.MoneyfyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyfyTheme {
                val navController = rememberNavController()
                val sharedViewModel: LoginViewModel = viewModel() // ✅ ViewModel chung

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController)
                    }
                    composable("signin") {
                        SignInScreen(navController, sharedViewModel)
                    }
                    composable("register") {
                        RegisterScreen(navController, sharedViewModel)
                    }
                    composable("home") {
                        HomeScreen(navController)
                    }
                }
            }
        }
    }
}
