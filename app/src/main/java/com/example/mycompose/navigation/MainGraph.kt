package com.example.mycompose.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mycompose.ui.screens.SecondScreen
import com.example.mycompose.ui.screens.ThirdScreen
import com.example.mycompose.ui.screens.home.HomeScreen


fun NavGraphBuilder.mainGraph(navController: NavController) {

    composable<Screen.HomeScreen> {
        HomeScreen(
            onNavigate = { imageURL ->
                navController.navigate(Screen.SecondScreen(imageURL))
            }
        )
    }

    composable<Screen.SecondScreen> {
        // Extracting the imageURL from the route arguments
        val args = it.toRoute<Screen.SecondScreen>()
        SecondScreen(
            imageURL = args.imageURL,
            onBack = {navController.navigateUp()},
            onNavigate = {
                navController.navigate(Screen.ThirdScreen) {
                }
            })
    }

    composable<Screen.ThirdScreen> {
        ThirdScreen(onBack = {
            // Navigate back to specific screen
            navController.popBackStack(Screen.HomeScreen, false)
        })
    }

}