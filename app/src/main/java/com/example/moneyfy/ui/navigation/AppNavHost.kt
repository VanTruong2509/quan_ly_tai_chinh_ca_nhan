package com.example.moneyfy.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.example.moneyfy.data.AppDatabase

import com.example.moneyfy.ui.screens.calendar.CalendarScreen
import com.example.moneyfy.ui.screens.home.HomeScreen
import com.example.moneyfy.ui.screens.login.*
import com.example.moneyfy.ui.screens.more.MoreScreen
import com.example.moneyfy.ui.screens.notification.NotificationScreen
import com.example.moneyfy.ui.screens.setting.SettingsScreen
import com.example.moneyfy.ui.screens.stats.StatsScreen
import com.example.moneyfy.ui.screens.balance.BalanceScreen

import com.example.moneyfy.ui.screens.add.AddTransactionScreen
import com.example.moneyfy.ui.screens.add.AddExpenseScreen
import com.example.moneyfy.ui.screens.add.CategoryListScreen
import com.example.moneyfy.ui.screens.add.SubCategoryScreen

import com.example.moneyfy.ui.screens.invest.InvestmentScreen
import com.example.moneyfy.ui.screens.invest.PlanningScreen
import com.example.moneyfy.ui.screens.account.CreateAccountScreen


@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val userDao = AppDatabase.getDatabase(context).userDao()

    val sharedViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(userDao)
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController, sharedViewModel) }
        composable("signin") { SignInScreen(navController, sharedViewModel) }

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

            composable("home") { HomeScreen(innerNavController) }
            composable("calendar") { CalendarScreen() }
            composable("statistics") { StatsScreen(innerNavController) }
            composable("balance") { BalanceScreen(innerNavController) }

            // More
            composable("more") {
                MoreScreen(
                    onSettingsClick = { innerNavController.navigate("settings") },
                    onInvestmentClick = { innerNavController.navigate("investment") },
                    onPlanningClick = { innerNavController.navigate("planning") } // ⭐ THÊM ROUTE
                )
            }

            // Settings
            composable("settings") {
                SettingsScreen(
                    onBackClick = { innerNavController.popBackStack() },
                    onItemClick = { println("Clicked: $it") }
                )
            }

            // Notification
            composable("notification") {
                NotificationScreen(
                    onBackClick = { innerNavController.popBackStack() }
                )
            }

            // Add transaction
            composable("add_transaction") { AddTransactionScreen(innerNavController) }
            composable("add_expense") { AddExpenseScreen(innerNavController) }
            composable("category_list") { CategoryListScreen(innerNavController) }

            // Sub Category
            composable(
                "sub_category/{id}/{name}",
                arguments = listOf(
                    navArgument("id") { type = androidx.navigation.NavType.IntType },
                    navArgument("name") { type = androidx.navigation.NavType.StringType }
                )
            ) { backStackEntry ->

                val catId = backStackEntry.arguments?.getInt("id") ?: 0
                val catName = backStackEntry.arguments?.getString("name") ?: ""

                SubCategoryScreen(
                    navController = innerNavController,
                    categoryId = catId,
                    categoryName = catName
                )
            }

            // ⭐ MÀN ĐẦU TƯ
            composable("investment") {
                InvestmentScreen(onBack = { innerNavController.popBackStack() })
            }

            // ⭐ MÀN LẬP KẾ HOẠCH
            composable("planning") {
                PlanningScreen(onBack = { innerNavController.popBackStack() })
            }
            // ⭐ MÀN TẠO TÀI KHOẢN
            composable("create_account") {
                CreateAccountScreen(onBack = { innerNavController.popBackStack() })
            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val items = listOf(
        BottomItem("home", "Tổng quan", Icons.Default.Home),
        BottomItem("calendar", "Lịch", Icons.Default.CalendarMonth),
        BottomItem("add_transaction", "Thêm", Icons.Default.AddCircle),
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
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route) Color.Green else Color.White
                    )
                },
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
