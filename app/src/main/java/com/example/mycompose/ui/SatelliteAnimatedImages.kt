package com.example.mycompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mycompose.ui.screens.splash.ModelImages
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SatelliteAnimatedImages(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    onClick: () -> Unit,
    users: ModelImages
) {
    var animationStarted by remember { mutableStateOf(false) }

    val radiusPx = with(LocalDensity.current) { 100.dp.toPx() }
    val visibleStates = remember(users.images.hashCode()) {
        users.images.map { mutableStateOf(false) }
    }

    val angleAnimList = remember(users.images.hashCode()) {
        users.images.map { Animatable(270f) }
    }

    val rotationJobs = remember(users.images.hashCode()) {
        mutableStateListOf<Job?>().apply { repeat(users.images.size) { add(null) } }
    }

    val imageSize by animateDpAsState(
        targetValue = if (animationStarted) 50.dp else 100.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "ImageSize"
    )

    val boxOffsetY by animateDpAsState(
        targetValue = if (animationStarted) (-80).dp else 0.dp,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "BoxOffsetY"
    )
    LaunchedEffect(users.images) {
        users.images.forEachIndexed { index, _ ->
            delay(150L)
            visibleStates[index].value = true
        }
    }

    DisposableEffect(users.images) {
        onDispose {
            rotationJobs.forEach { it?.cancel() }
        }
    }

    LaunchedEffect(animationStarted, users.images) {
        if (animationStarted && users.images.isNotEmpty()) {
            users.images.forEachIndexed { index, _ ->
                val targetAngle = 270f + index * (380f / users.images.size)
                launch {
                    angleAnimList[index].animateTo(
                        targetAngle,
                        animationSpec = tween(1000, easing = FastOutSlowInEasing)
                    )
                    val job = launch {
                        while (animationStarted) {
                            val newAngle = (angleAnimList[index].value + 0.50f) % 360f
                            angleAnimList[index].snapTo(newAngle)
                            delay(20)
                        }
                    }
                    rotationJobs[index] = job
                }
                delay(100)
            }
        } else {
            rotationJobs.forEach { it?.cancel() }
        }
    }

    Box(modifier = modifier.offset(y = boxOffsetY)) {

        users.images.forEachIndexed { index, user ->
            val angleDegrees = angleAnimList[index].value
            val angleRadians = Math.toRadians(angleDegrees.toDouble())
            val x = radiusPx * cos(angleRadians).toFloat()
            val y = radiusPx * sin(angleRadians).toFloat()

            AnimatedVisibility(
                visible = visibleStates[index].value,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                AsyncImage(
                    model = user,
                    contentDescription = "User Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier

                        .graphicsLayer {
                            translationX = x
                            translationY = y
                        }
                        .animateEnterExit(
                            enter = fadeIn(
                                initialAlpha = 0f,
                                animationSpec = tween(300)
                            ),
                            exit = fadeOut(
                                targetAlpha = 0f,
                                animationSpec = tween(300)
                            )
                        )
                        .size(imageSize)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .clickable {
                            if (!animationStarted) animationStarted = true
                            onClick()
                        }
                )
            }
        }
    }

}