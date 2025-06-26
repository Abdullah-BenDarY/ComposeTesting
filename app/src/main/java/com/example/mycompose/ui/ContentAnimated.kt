package com.example.mycompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycompose.ui.screens.home.ActionButton

@Composable
fun ContentAnimated(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    animationStarted: Boolean,
    onNext: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val infiniteSlide by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse
        )
    )

    AnimatedVisibility(
        modifier = modifier,
        visible = animationStarted,
        enter = expandIn(expandFrom = Alignment.Center) + scaleIn(),
        exit = shrinkOut(shrinkTowards = Alignment.Center) + scaleOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ActionButton(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(50.dp),
                onClick = { onNext() },
                contentColor = if (isDark) Color.Black else Color.White,
                containerColor = if (isDark) Color.White else Color.Black,
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Lets go",

                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally(initialOffsetX = { -it * 5 }),
                                    exit = slideOutHorizontally(targetOffsetX = { it * 5 })
                                )
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "arrow",
                            modifier = Modifier
                                .offset(x = infiniteSlide.dp)
                        )

//                        Icon(
//                            imageVector = Icons.Default.ArrowForward,
//                            contentDescription = "arrow",
//                            modifier = Modifier
//                                .graphicsLayer {
//                                    scaleX = 1f + (infiniteSlide / 10f)
//                                    transformOrigin = TransformOrigin(0f, 0.5f)
//                                }
//                        )
                    }
                }
            )
            Text(
                text = "Meet the voices shaping the future—one episode at a time.",
                color = if (isDark) Color.White else Color.Black,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.animateEnterExit(
                    enter = slideInHorizontally(initialOffsetX = { -it * 5 }),
                    exit = slideOutHorizontally(targetOffsetX = { it * 5 })
                )
            )
            Text(
                text = "Your next obsession is just a play button away. \n Tune in now.",
                color = Color.Gray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .animateEnterExit(
                        enter = slideInHorizontally(initialOffsetX = { -it * 5 }),
                        exit = slideOutHorizontally(targetOffsetX = { it * 5 })
                    )
            )

        }
    }
}