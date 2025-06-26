package com.example.mycompose.ui.screens.splash

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.mycompose.ui.ContentAnimated
import com.example.mycompose.ui.FadingLottieAnimation
import com.example.mycompose.ui.SatelliteAnimatedImages
import com.example.mycompose.ui.TextSwitcher

@Composable
fun Splash(
    onNext: () -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel(),
) {
    val isDark = isSystemInDarkTheme()
//    val sensorData = rememberSensorRotation()
    var animationStarted by remember { mutableStateOf(false) }
    val data by splashViewModel.images.collectAsState()
    val isLoading by splashViewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) { splashViewModel.loadData() }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("Animation-loading.json")
    )


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = if (isDark) Color.Black else Color.White,
    ) { padding ->
//        MotionBackground(
//            modifier = Modifier.fillMaxSize(),
//            sensorData = sensorData.value,
//            isDark = isDark
//        )
        Box(
            modifier = Modifier
                .padding(padding)
                .systemBarsPadding()
                .fillMaxSize()
        ) {
            FadingLottieAnimation(
                modifier = Modifier.align(Alignment.Center),
                isLoading = isLoading,
                composition=composition)

                if (isLoading == false){
                    TextSwitcher(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 100.dp),
                        isDark = isDark,
                        texts = data,
                        animationStarted = animationStarted,
                    )

                    SatelliteAnimatedImages(
                        modifier = Modifier.align(Alignment.Center),
                        isDark = isDark,
                        users = data,
                        onClick = {
                            if (!animationStarted) animationStarted = true
                        }
                    )
                }


            ContentAnimated(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                isDark = isDark,
                animationStarted = animationStarted,
                onNext = {
                    onNext()
                }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularSequentialAnimation() {
    Splash(onNext = {})
}

