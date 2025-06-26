package com.example.mycompose.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.mycompose.ui.screens.splash.ModelImages
import kotlinx.coroutines.delay

@Composable
fun TextSwitcher(
    modifier: Modifier = Modifier,
    animationStarted: Boolean,
    isDark: Boolean,
    texts: ModelImages,
    switchDelay: Long = 2000L
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var displayChars by remember { mutableStateOf<List<Char>>(emptyList()) }
    var isFirstVisible by remember { mutableStateOf(false) }
    val currentText = texts.strings[currentIndex]
    val nextText = texts.strings[(currentIndex + 1) % texts.strings.size]

    fun padToSameLength(a: String, b: String): Pair<String, String> {
        val maxLength = maxOf(a.length, b.length)
        return a.padEnd(maxLength) to b.padEnd(maxLength)
    }

    LaunchedEffect(Unit) {
        delay(1500)
        isFirstVisible = true
    }

    LaunchedEffect(currentIndex, animationStarted) {
        if (!animationStarted) {
            val (fromText, toText) = padToSameLength(currentText, nextText)
            for (i in fromText.indices) {
                val updatedChars = toText.mapIndexed { index, targetChar ->
                    if (index <= i) targetChar else fromText[index]
                }
                displayChars = updatedChars
                delay(120)
            }
            delay(switchDelay)
            currentIndex = (currentIndex + 1) % texts.strings.size
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isFirstVisible && !animationStarted,
        enter = fadeIn(tween(500)) + slideInVertically { it / 4 },
        exit = fadeOut(tween(500)) + slideOutVertically { -it / 4 }
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            displayChars.forEachIndexed { index, char ->
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        (slideInVertically { it / 2 } + fadeIn(tween(150, delayMillis = 50)))
                            .togetherWith(slideOutVertically { -it / 2 } + fadeOut(tween(150)))
                    },
                    label = "CharAnimation"
                ) { animatedChar ->
                    Text(
                        text = animatedChar.toString(),
                        color = if (isDark) Color.White else Color.Black,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Cursive,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}