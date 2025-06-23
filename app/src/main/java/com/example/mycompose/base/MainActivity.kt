package com.example.mycompose.base

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import com.example.mycompose.navigation.MainNavigation
import com.example.mycompose.ui.theme.MyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeTheme {
                MainNavigation()
//                // Remember a SystemUiController
//                val systemUiController = rememberSystemUiController()
//                val useDarkIcons = !isSystemInDarkTheme()
//
//                DisposableEffect(systemUiController, useDarkIcons) {
//                    // Update all of the system bar colors to be transparent, and use
//                    // dark icons if we're in light theme
//                    systemUiController.setSystemBarsColor(
//                        color = Color.TRANSPARENT,
//                        darkIcons = useDarkIcons
//                    )
//
//                    // setStatusBarColor() and setNavigationBarColor() also exist
//
//                    onDispose {}
//                }
            }

        }
    }
}