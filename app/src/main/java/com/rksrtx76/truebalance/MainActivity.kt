package com.rksrtx76.truebalance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.rksrtx76.truebalance.presentation.MainViewModel
import com.rksrtx76.truebalance.presentation.NavHostScreen
import com.rksrtx76.truebalance.presentation.ThemeViewModel
import com.rksrtx76.truebalance.ui.theme.LightGray
import com.rksrtx76.truebalance.ui.theme.TrueBalanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isReady.value
            }
        }
        enableEdgeToEdge()
        setContent {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            TrueBalanceTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = LightGray
                ) { innerPadding ->
                    NavHostScreen(
                        themeViewModel = themeViewModel,
                        isDarkTheme = isDarkTheme,
                    )
                }
            }
        }
    }
}
