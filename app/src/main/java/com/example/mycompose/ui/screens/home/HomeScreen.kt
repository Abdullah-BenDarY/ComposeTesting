package com.example.mycompose.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.mycompose.ui.utils.showMessage

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
            modifier = Modifier.fillMaxSize()
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
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White)
                        .padding(horizontal = 8.dp)
                ) {
                    val (increment, decrement, reset, navigation , counter, image) = createRefs()
                    val horizontalGuideline = createGuidelineFromTop(0.50f) // 50% (just for example)
                    createHorizontalChain(increment, decrement, chainStyle = ChainStyle.Spread)

                    CounterImage(
                        painter = data.value.imageURL,
                        modifier = Modifier.constrainAs(image) {
                            top.linkTo(parent.top)
                            bottom.linkTo(counter.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
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
                        content = "Increment"
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
                        content = "Decrement"
                    )

                    // Reset Button
                    ActionButton(
                        modifier = Modifier
                            .constrainAs(reset) {
                                top.linkTo(increment.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(8.dp),
                        onClick = viewModel::reset,
                        contentColor = Color.White,
                        containerColor = Color.LightGray,
                        content = "Reset")

                    // Navigation Button
                    ActionButton(
                        modifier = Modifier
                            .constrainAs(navigation) {
                                top.linkTo(reset.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        onClick = {onNavigate(data.value.imageURL ?: "")},
                        contentColor = Color.White,
                        containerColor = Color.LightGray,
                        content = "Next")

                }

                ShowMessage(
                    viewModel = viewModel,
                    snackBarHostState = snackBarHostState
                )
            }
        }

    }
}


@Composable
private fun CounterImage(painter: String?, modifier: Modifier) {
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


@Composable
private fun ShowMessage(
    viewModel: HomeViewModel,
    snackBarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            showMessage(
                message = message.toastMessage,
                snackBarHostState = snackBarHostState,
                context = context,
                scope = scope
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onNavigate = {})
}