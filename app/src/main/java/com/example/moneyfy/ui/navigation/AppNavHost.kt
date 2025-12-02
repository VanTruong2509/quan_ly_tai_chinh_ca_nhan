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
import com.example.moneyfy.ui.screens.add.*
import com.example.moneyfy.ui.screens.account.CreateAccountScreen
import com.example.moneyfy.ui.screens.calendar.CalendarScreen
import com.example.moneyfy.ui.screens.home.HomeScreen
import com.example.moneyfy.ui.screens.home.HomeViewModel
import com.example.moneyfy.ui.screens.invest.InvestmentScreen
import com.example.moneyfy.ui.screens.invest.PlanningScreen
import com.example.moneyfy.ui.screens.login.*
import com.example.moneyfy.ui.screens.more.MoreScreen
import com.example.moneyfy.ui.screens.notification.NotificationScreen
import com.example.moneyfy.ui.screens.setting.SettingsScreen
import com.example.moneyfy.ui.screens.stats.StatsScreen
import com.example.moneyfy.ui.screens.balance.BalanceScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val sharedLoginViewModel: LoginViewModel =
        viewModel(factory = LoginViewModelFactory(userDao))

    NavHost(navController = navController, startDestination = "login") {
        // --- Login / Register / SignIn ---
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController, sharedLoginViewModel) }
        composable("signin") { SignInScreen(navController, sharedLoginViewModel) }

        // --- Forgot Password ---
        composable("forgot_password") { ForgotPasswordScreen(navController, sharedLoginViewModel) }

        // --- Main Navigation ---
        composable("main") { MainNavigation(navController) }

        // --- AddExpense với query category ---
        composable(
            route = "add_expense?category={category}",
            arguments = listOf(navArgument("category") {
                type = androidx.navigation.NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val homeViewModel: HomeViewModel = viewModel() // shared ViewModel
            AddExpenseScreen(navController, homeViewModel, selectedCategory = category)
        }

        // --- Category List ---
        composable("category_list") { CategoryListScreen(navController) }

        // --- Sub Category ---
        composable(
            "sub_category/{id}/{name}",
            arguments = listOf(
                navArgument("id") { type = androidx.navigation.NavType.IntType },
                navArgument("name") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val catId = backStackEntry.arguments?.getInt("id") ?: 0
            val catName = backStackEntry.arguments?.getString("name") ?: ""
            SubCategoryScreen(navController, catId, catName)
        }
    }
}

// --- Main Navigation ---
@Composable
fun MainNavigation(rootNavController: NavHostController) {
    val innerNavController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    Scaffold(
        bottomBar = { BottomNavigationBar(innerNavController) },
        containerColor = Color(0xFF101010)
    ) { padding ->
        NavHost(
            navController = innerNavController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(innerNavController, homeViewModel) }
            composable("calendar") { CalendarScreen(spendings = homeViewModel.spendings.collectAsState().value) }
            composable("statistics") { StatsScreen(innerNavController, homeViewModel) }
            composable("balance") { BalanceScreen(innerNavController) }

            composable("more") {
                MoreScreen(
                    onSettingsClick = { innerNavController.navigate("settings") },
                    onInvestmentClick = { innerNavController.navigate("investment") },
                    onPlanningClick = { innerNavController.navigate("planning") }
                )
            }

            composable("settings") {
                SettingsScreen(
                    navController = rootNavController, // <--- dùng rootNavController để quay về login
                    onBackClick = { innerNavController.popBackStack() },
                    onItemClick = { println("Clicked: $it") }
                )
            }


            composable("notification") {
                NotificationScreen(
                    homeViewModel = homeViewModel,
                    onBackClick = { innerNavController.popBackStack() }
                )
            }

            composable("add_transaction") { AddTransactionScreen(innerNavController) }

            composable(
                route = "add_expense?category={category}",
                arguments = listOf(navArgument("category") {
                    type = androidx.navigation.NavType.StringType
                    defaultValue = ""
                    nullable = true
                })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                AddExpenseScreen(innerNavController, homeViewModel, selectedCategory = category)
            }

            composable("category_list") { CategoryListScreen(innerNavController) }

            composable(
                "sub_category/{id}/{name}",
                arguments = listOf(
                    navArgument("id") { type = androidx.navigation.NavType.IntType },
                    navArgument("name") { type = androidx.navigation.NavType.StringType }
                )
            ) { backStackEntry ->
                val catId = backStackEntry.arguments?.getInt("id") ?: 0
                val catName = backStackEntry.arguments?.getString("name") ?: ""
                SubCategoryScreen(innerNavController, catId, catName)
            }

            composable("investment") { InvestmentScreen(onBack = { innerNavController.popBackStack() }) }
            composable("planning") { PlanningScreen(onBack = { innerNavController.popBackStack() }) }
            composable("create_account") { CreateAccountScreen(onBack = { innerNavController.popBackStack() }) }
        }
    }
}

// --- Bottom Navigation ---
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
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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

data class BottomItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
