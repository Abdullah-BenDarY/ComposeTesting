package com.example.mycompose.ui.screens.splash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.mycompose.R
import com.example.mycompose.ui.ContentAnimated
import com.example.mycompose.ui.FadingLottieAnimation
import com.example.mycompose.ui.SatelliteAnimatedImages
import com.example.mycompose.ui.ShowDialog
import com.example.mycompose.ui.TextSwitcher



@Composable
fun Splash(
    onNext: () -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val requestGPSPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* ignore result for now */ }

//    val requestGPSPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestMultiplePermissions(),
//        onResult = { permissions ->
//            val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
//            val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION]
//
//
//
//        }
//    )

    fun isGPSPermissionAllowed(request: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            request
        ) == PackageManager.PERMISSION_GRANTED
    }
    var hasRequestedPermissionOnce by rememberSaveable { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val showOpenSettingsDialog = remember { mutableStateOf(false) }
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

                    when {
                        // Already granted
                        isGPSPermissionAllowed(Manifest.permission.ACCESS_FINE_LOCATION) ||
                                isGPSPermissionAllowed(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                            onNext()
                        }

                        // First time asking (rationale false AND we never asked before)
                        !shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION) &&
                                !shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                                !hasRequestedPermissionOnce -> {
                            hasRequestedPermissionOnce = true
                            requestGPSPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }

                        // User denied previously but didn't select "Don't ask again"
                        shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION) ||
                                shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                            showDialog.value = true
                        }

                        // User denied with "Don't ask again"
                        else -> {
                            showOpenSettingsDialog.value = true
                        }
                    }                }
            )

        }
        if (showDialog.value) {
            ShowDialog(
                onDismissRequest = { showDialog.value = false },
                onConfirmation = {
                    showDialog.value = false
                    requestGPSPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                dialogTitle = "Permission",
                dialogText = "We need to access your location to get the nearest driver",
                confirmButtonText = "Confirm",
                dismissButtonText = "Dismiss",
                icon = R.drawable.ic_launcher_foreground
            )
        }
        if (showOpenSettingsDialog.value) {
            ShowDialog(
                onDismissRequest = { showOpenSettingsDialog.value = false },
                onConfirmation = {
                    showOpenSettingsDialog.value = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        val uri = Uri.fromParts("package", context.packageName, null)
                        this.data = uri
                    }

                    context.startActivity(intent)
                },
                dialogTitle = "Permission required",
                dialogText = "You have permanently denied location access. Please enable it from Settings.",
                confirmButtonText = "Open Settings",
                dismissButtonText = "Cancel",
                icon = R.drawable.ic_launcher_foreground
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularSequentialAnimation() {
    Splash(onNext = {})
}

