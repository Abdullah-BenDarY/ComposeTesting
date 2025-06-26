package com.example.mycompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants

@Composable
fun FadingLottieAnimation(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    composition: LottieComposition?
) {
    val alpha = remember { Animatable(if (isLoading) 0f else 0f) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
        } else {
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isLoading,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000, easing = LinearEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000, easing = LinearEasing))
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier =Modifier
                .size(100.dp)
                .alpha(alpha.value)
        )
    }
}