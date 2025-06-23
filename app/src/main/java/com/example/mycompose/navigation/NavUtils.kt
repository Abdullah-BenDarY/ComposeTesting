package com.example.mycompose.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object HomeScreen : Screen

    @Serializable
    data class SecondScreen(val imageURL: String) : Screen

    @Serializable
    data object ThirdScreen : Screen
}
