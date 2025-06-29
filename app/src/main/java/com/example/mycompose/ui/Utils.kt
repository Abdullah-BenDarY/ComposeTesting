package com.example.mycompose.ui

import androidx.annotation.DrawableRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@Composable
fun ShowDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    @DrawableRes icon: Int
) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Example Icon"
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            if (dismissButtonText != null) {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(dismissButtonText)
                }
            }
        }
    )
}

