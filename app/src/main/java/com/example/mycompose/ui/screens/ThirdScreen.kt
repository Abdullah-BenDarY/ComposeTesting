package com.example.mycompose.ui.screens

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng


@Composable
fun ThirdScreen(onBack: () -> Unit) {

    Scaffold { paddingValues ->
        Map(modifier = Modifier.padding(paddingValues))
    }
}

@SuppressLint("MissingPermission")
@Composable
fun Map(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationState = remember { mutableStateOf<Location?>(null) }
    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    val locationRequest = remember {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0L)
            .setMinUpdateIntervalMillis(0L)
            .setMinUpdateDistanceMeters(1f)
            .build()
    }

    val locationCallback = rememberUpdatedState(
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach {
                    locationState.value = it
                    Log.d("Location", "Updated: $it")
                }
            }
        }
    )

    // MapView lifecycle
    DisposableEffect(lifecycle, mapView) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) = mapView.onStart()
            override fun onResume(owner: LifecycleOwner) = mapView.onResume()
            override fun onPause(owner: LifecycleOwner) = mapView.onPause()
            override fun onStop(owner: LifecycleOwner) = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner) = mapView.onDestroy()
        }

        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    // Subscribe to location updates
    DisposableEffect(Unit) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback.value,
            Looper.getMainLooper()
        )

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback.value)
        }
    }

    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var isCameraMoved by remember { mutableStateOf(false) }

    var selectedMapType by remember { mutableIntStateOf(GoogleMap.MAP_TYPE_NORMAL) }
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize()) { view ->
            view.getMapAsync { map ->
                googleMap = map
                map.uiSettings.isZoomControlsEnabled = true
                map.isTrafficEnabled = true
                map.isMyLocationEnabled = true
                map.mapType = selectedMapType
            }
        }

        MapTypeDropdown(
            selectedMapType = selectedMapType,
            onMapTypeSelected = {
                selectedMapType = it
                googleMap?.mapType = it
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 100.dp)
        )

    }

    LaunchedEffect(locationState.value) {
        val loc = locationState.value ?: return@LaunchedEffect
        val newLatLng = LatLng(loc.latitude, loc.longitude)

        googleMap?.let { map ->
            if (!isCameraMoved) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 18f))
                isCameraMoved = true
            }

        }
    }
}
@Composable
fun MapTypeDropdown(
    selectedMapType: Int,
    onMapTypeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val mapTypes = listOf(
        "Normal" to GoogleMap.MAP_TYPE_NORMAL,
        "Satellite" to GoogleMap.MAP_TYPE_SATELLITE,
        "Terrain" to GoogleMap.MAP_TYPE_TERRAIN,
        "Hybrid" to GoogleMap.MAP_TYPE_HYBRID
    )
    val selectedIcon = when (selectedMapType) {
        GoogleMap.MAP_TYPE_NORMAL -> Icons.Default.Map
        GoogleMap.MAP_TYPE_SATELLITE -> Icons.Default.Satellite
        GoogleMap.MAP_TYPE_TERRAIN -> Icons.Default.Terrain
        GoogleMap.MAP_TYPE_HYBRID -> Icons.Default.Layers
        GoogleMap.MAP_TYPE_NONE -> Icons.Default.Block
        else -> Icons.Default.Map
    }



    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.White
            ),
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = selectedIcon,
                contentDescription = "Map Type Icon",
                modifier = Modifier.size(24.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            mapTypes.forEach { (label, type) ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        onMapTypeSelected(type)
                        expanded = false
                    }
                )

            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DropdownPreview() {
    MapTypeDropdown(
        selectedMapType = 1,
        onMapTypeSelected = {}
    )
}