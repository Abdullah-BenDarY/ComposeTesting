package com.example.mycompose.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.mycompose.ui.ShowMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    // Using hiltViewModel to get the instance of HomeViewModel
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    // Collecting the count state from the ViewModel
    val data = viewModel.count.collectAsState()
    // Creating a SnackBarHostState to manage SnackBar messages
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        // Using Scaffold to provide snackBar support
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        // Using LazyColumn to display a list of items (Just for lazy column Testing)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            // Adding a sticky header at the top of the LazyColumn
            stickyHeader {
                Text(
                    text = "First",
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
            }
            item {
                // Using ConstraintLayout to arrange the UI elements
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White)
                        .padding(horizontal = 8.dp)
                ) {
                    val (increment, decrement, reset, navigation , counter, image) = createRefs()
                    val horizontalGuideline = createGuidelineFromTop(0.50f) // 50% (just for example)
                    createHorizontalChain(increment, decrement, chainStyle = ChainStyle.Spread)

                    CounterImage(
                        painter = data.value.imageURL,
                        modifier = Modifier
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(8.dp)
                    )

                    CounterText(
                        count = data.value.count,
                        modifier = Modifier.constrainAs(counter) {
                            top.linkTo(image.bottom)
                            bottom.linkTo(increment.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                    // Increment Button
                    ActionButton(
                        modifier = Modifier
                            .constrainAs(increment) {
                                top.linkTo(counter.bottom)
                            },
                        onClick = viewModel::increment,
                        contentColor = Color.White,
                        containerColor = Color.Green,
                        content = {Text("Increment")}
                    )

                    // Decrement Button
                    ActionButton(
                        modifier = Modifier
                            .constrainAs(decrement) {
                                top.linkTo(increment.top)
                                bottom.linkTo(increment.bottom)
                            },
                        onClick =viewModel::decrement,
                        contentColor = Color.White,
                        containerColor = Color.Red,
                        content = {
                            Text("Decrement")
                        }

                    )

                    // Reset Button
                    val shouldShowReset = data.value.count != 0
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = CenterHorizontally,
                        modifier = Modifier
                            .constrainAs(reset) {
                                top.linkTo(increment.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(8.dp)) {
                        // Main animation
                        AnimatedVisibility(
                            visible = shouldShowReset,
                            // Complex Animation
                            enter = expandIn(expandFrom = Alignment.Center) + scaleIn(),
                            exit = shrinkOut(shrinkTowards = Alignment.Center)+ scaleOut()
                        ) {
                            ActionButton(
                                onClick = viewModel::reset,
                                contentColor = Color.White,
                                containerColor = Color.LightGray,
                                content = {
                                    Text(
                                        text = "Reset",
                                        modifier = Modifier.animateEnterExit(
                                            //Nested animation
                                            // X == horizontalScroll
                                            // Y == verticalScroll
                                            enter = slideInHorizontally(initialOffsetX = { -it * 5 }),
                                            exit = slideOutHorizontally(targetOffsetX = { it * 5 })
                                        )
                                    )
                                }
                            )
                        }
                        // Navigation Button
                        ActionButton(
                            onClick = { onNavigate(data.value.imageURL ?: "") },
                            contentColor = Color.White,
                            containerColor = Color.LightGray,
                            content = {
                                Text("Next")
                            }
                        )
                    }

                }

                ShowMessage(
                    message = data.value.toastMessage.toString(),
                    snackBarHostState = snackBarHostState
                )
            }

        }

    }
}

@Composable
fun CircularMotionComposable(painter: String?, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val radius = 50.dp
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CounterImage(painter)

        // العنصر المتحرك
        Box(
            modifier = Modifier
                .offset {
                    val x =
                        (radius.toPx() * kotlin.math.cos(Math.toRadians(angle.toDouble()))).toInt()
                    val y =
                        (radius.toPx() * kotlin.math.sin(Math.toRadians(angle.toDouble()))).toInt()
                    IntOffset(x, y)
                }
                .size(40.dp)
                .background(Color.Red, CircleShape)
        )
    }
}


@Composable
private fun CounterImage(painter: String?, modifier: Modifier = Modifier) {
    // Using AsyncImage from coil to load an image from a URL
    AsyncImage(
        model = painter?: "",
        contentDescription = "Counter Image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    )
}

@Composable
private fun CounterText(count: Int, modifier: Modifier) {
    Text(
        text = count.toString(),
        color = Color.Black,
        fontSize = 40.sp,
        modifier = modifier
    )
}

@Composable
@Stable
fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentColor: Color = Color.White,
    containerColor: Color,
    content: @Composable () -> Unit
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
        content = {
            content()
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onNavigate = {})
}