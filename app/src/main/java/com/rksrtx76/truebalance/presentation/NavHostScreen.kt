package com.rksrtx76.truebalance.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rksrtx76.truebalance.R
import com.rksrtx76.truebalance.presentation.AddScreen.AddTransactionScreen
import com.rksrtx76.truebalance.presentation.AnalyticsScreen.AnalyticScreen
import com.rksrtx76.truebalance.presentation.HomeScreen.HomeScreen
import com.rksrtx76.truebalance.presentation.ProfileScreen.ProfileScreen
import com.rksrtx76.truebalance.presentation.TransactionScreen.AllTransactionsScreen
import com.rksrtx76.truebalance.presentation.TransactionScreen.TransactionDetailScreen
import com.rksrtx76.truebalance.ui.theme.AzureBlue
import com.rksrtx76.truebalance.util.BottomNav
import com.rksrtx76.truebalance.util.Screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostScreen(
    themeViewModel: ThemeViewModel,
    isDarkTheme : Boolean
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItem = rememberSaveable { mutableStateOf(0) }
    val viewModel = hiltViewModel<TransactionViewModel>()


    Scaffold(
        bottomBar = {
           // If current route is transaction screen then hide bottom bar
            if(currentRoute != Screens.ADD_TRANSACTION_SCREEN && currentRoute != Screens.ALL_TRANSACTIONS_SCREEN){
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    bottomNavController = navController
                )
            }
        },
        floatingActionButton = {
            // If current route is home then only show floating action button
            if (currentRoute == BottomNav.HOME_SCREEN){
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screens.ADD_TRANSACTION_SCREEN)
                    },
                    contentColor = Color.White,
                    containerColor = AzureBlue
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
        }
    ) { innerPadding->

        NavHost(
            navController = navController,
            startDestination = BottomNav.HOME_SCREEN
        ){
            composable(Screens.HOME_SCREEN) {
                HomeScreen(
                    viewModel = viewModel,
                    navController = navController,
                    isDarkTheme = isDarkTheme
                )
            }
            composable(Screens.ADD_TRANSACTION_SCREEN) {
                AddTransactionScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(BottomNav.HISTORY_SCREEN) {
                AllTransactionsScreen(
                    navController = navController,
                    viewModel = viewModel,
                    paddingValues = innerPadding
                )
            }

            composable(
                route = Screens.TRANSACTION_DETAIL_SCREEN + "/{transactionId}",
                arguments = listOf(
                    navArgument("transactionId"){
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0

                TransactionDetailScreen(
                    transactionId = transactionId,
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(BottomNav.ANALYTICS_SCREEN) {
                AnalyticScreen(
                    viewModel = viewModel,
                    navController = navController,
                    paddingValues = innerPadding
                )
            }
            composable(BottomNav.PROFILE_SCREEN) {
                ProfileScreen(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = {
                        themeViewModel.toggleTheme()
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}