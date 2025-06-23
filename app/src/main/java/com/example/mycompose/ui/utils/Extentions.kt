package com.example.mycompose.ui.utils

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showMessage(
    message: Any?,
    snackBarHostState: SnackbarHostState,
    context: Context,
    scope: CoroutineScope
) {
    message?.let {
        val text = it.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            scope.launch {
                snackBarHostState.showSnackbar(text)
            }
        } else {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }
}


