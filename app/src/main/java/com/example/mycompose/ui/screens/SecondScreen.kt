package com.example.mycompose.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun SecondScreen(
    onNavigate: () -> Unit,
    imageURL: String? = null,
) {
    Scaffold { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val (button, webView) = createRefs()
            WebPageScreen(
                modifier = Modifier.constrainAs(webView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(button.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.fillMaxWidth(),
                url = "https://google.com"
            )
            ActionButton(
                modifier = Modifier
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onClick = onNavigate,
                containerColor = Color.Magenta,
                contentColor = Color.White,
                content = "Go to Third Screen"
            )
        }


    }
}


@Composable
@Stable
private fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentColor: Color = Color.White,
    containerColor: Color,
    content: String
) {
    // Using rememberUpdatedState to ensure the onClick lambda is always up-to-date and does not cause recomposition issues
    val currentOnClick by rememberUpdatedState(onClick)
    Button(
        onClick = currentOnClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        content = { Text(content) }
    )
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPageScreen(
    modifier: Modifier = Modifier,
    url: String
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    SecondScreen(onNavigate = {})
}