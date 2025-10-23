package com.example.moneyfy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moneyfy.ui.screens.login.LoginScreen
import com.example.moneyfy.ui.screens.login.RegisterScreen
import com.example.moneyfy.ui.screens.login.SignInScreen
import com.example.moneyfy.ui.screens.home.HomeScreen
import com.example.moneyfy.ui.screens.login.LoginViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    // ✅ Tạo ViewModel dùng chung cho toàn bộ NavGraph
    val sharedViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController, sharedViewModel) }  // ✅ truyền viewModel
        composable("signin") { SignInScreen(navController, sharedViewModel) }       // ✅ truyền viewModel
        composable("home") { HomeScreen(navController) }
    }
}
