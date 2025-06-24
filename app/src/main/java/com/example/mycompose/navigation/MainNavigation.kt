package com.example.mycompose.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavigation(
) {
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = Screen.HomeScreen,
            enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { fullWidth -> fullWidth } },
            exitTransition = { fadeOut(tween(300)) + slideOutHorizontally(tween(300)) { fullWidth -> -fullWidth } },
            popEnterTransition = { slideInHorizontally(tween(300)) { fullWidth -> -fullWidth } + fadeIn(tween(300)) },
            popExitTransition = { slideOutHorizontally(tween(300)) { fullWidth -> fullWidth } + fadeOut(tween(300)) }
        ) {
            mainGraph(navController = navController)
        }
    }
}