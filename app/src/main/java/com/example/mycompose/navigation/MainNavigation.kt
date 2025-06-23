package com.example.mycompose.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MainNavigation(
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

        }
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.HomeScreen
        ) {
            mainGraph(navController = navController)
        }
    }
}