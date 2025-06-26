package com.example.mycompose.navigation

import android.annotation.SuppressLint
import android.view.Window
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation(window: Window) {
    val navController = rememberNavController()
    val isDark = isSystemInDarkTheme()
    val statusBarColor by rememberUpdatedState(if (isDark) Color.Black else Color.White)

    Scaffold(modifier = Modifier.fillMaxSize()) { _->
        Box{
            SystemBarsBackgrounds(
                statusBarColor = statusBarColor,
                navigationBarColor = statusBarColor
            )
            SetSystemBarIconColors(window)
            NavHost(
                navController = navController,
                startDestination = Screen.Splash,
                enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { fullWidth -> fullWidth } },
                exitTransition = { fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { fullWidth -> -fullWidth } },
                popEnterTransition = { slideInHorizontally(tween(300)) { fullWidth -> -fullWidth } + fadeIn(tween(300)) },
                popExitTransition = { slideOutHorizontally(tween(300)) { fullWidth -> fullWidth } + fadeOut(tween(300)) }
            ) {
                mainGraph(navController = navController)
            }
        }
    }
}

@Composable
fun SystemBarsBackgrounds(
    statusBarColor: Color,
    navigationBarColor: Color
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Status bar background
        Box(
            Modifier
                .fillMaxWidth()
                .background(statusBarColor)
                .windowInsetsPadding(WindowInsets.statusBars)
        )

        // Navigation bar background
        Box(
            Modifier
                .fillMaxWidth()
                .background(navigationBarColor)
                .windowInsetsPadding(WindowInsets.navigationBars)
        )
    }
}
@Composable
fun SetSystemBarIconColors(window: Window) {
    val isDarkTheme = isSystemInDarkTheme()
    val view = LocalView.current
    SideEffect {
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = !isDarkTheme
        insetsController.isAppearanceLightNavigationBars = !isDarkTheme
    }
}


