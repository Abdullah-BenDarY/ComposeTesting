package com.example.mycompose.navigation

import android.annotation.SuppressLint
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

        }
    ) {innerPadding ->
        val navController = rememberNavController()
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.HomeScreen
        ) {
            mainGraph(navController = navController)
        }
    }
}