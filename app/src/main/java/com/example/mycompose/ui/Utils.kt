package com.example.mycompose.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.mycompose.ui.utils.showMessage

@Composable
fun ShowMessage(
    message: String,
    snackBarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(message) {
        showMessage(
            message = message,
            snackBarHostState = snackBarHostState,
            context = context,
            scope = scope
        )
    }
}

