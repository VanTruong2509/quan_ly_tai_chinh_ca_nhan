package com.example.moneyfy.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.moneyfy.ui.screens.calendar.CalendarScreen
import com.example.moneyfy.ui.screens.home.HomeScreen
import com.example.moneyfy.ui.screens.login.*
import com.example.moneyfy.ui.screens.more.MoreScreen
import com.example.moneyfy.ui.screens.setting.SettingsScreen
import com.example.moneyfy.ui.screens.stats.StatisticsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val sharedViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // ==== NHÓM MÀN HÌNH LOGIN ====
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController, sharedViewModel) }
        composable("signin") { SignInScreen(navController, sharedViewModel) }

        // ==== SAU KHI ĐĂNG NHẬP ====
        composable("main") {
            MainNavigation(navController)
        }
    }
}

@Composable
fun MainNavigation(rootNavController: NavHostController) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(innerNavController) },
        containerColor = Color(0xFF101010)
    ) { padding ->
        NavHost(
            navController = innerNavController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            // 👉 Màn hình chính
            composable("home") { HomeScreen(innerNavController) }

            // 👉 Màn hình lịch
            composable("calendar") { CalendarScreen() }

            // 👉 Màn hình thống kê
            composable("statistics") { StatisticsScreen() }

            // 👉 Màn hình “Thêm”
            composable("more") {
                MoreScreen(
                    onSettingsClick = {
                        innerNavController.navigate("settings")
                    }
                )
            }

            // 👉 Màn hình “Cài đặt”
            composable("settings") {
                SettingsScreen(
                    onBackClick = { innerNavController.popBackStack() },
                    onItemClick = { item ->
                        println("Bạn đã chọn: $item")
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomItem("home", "Tổng quan", Icons.Default.Home),
        BottomItem("calendar", "Lịch", Icons.Default.CalendarMonth),
        BottomItem("statistics", "Thống kê", Icons.Default.BarChart),
        BottomItem("more", "Thêm", Icons.Default.MoreHoriz)
    )

    NavigationBar(containerColor = Color.Black) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
                label = { Text(item.title, color = Color.White) }
            )
        }
    }
}

data class BottomItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
