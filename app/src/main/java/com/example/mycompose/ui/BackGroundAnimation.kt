package com.example.mycompose.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.lerp

@Composable
fun MotionBackground(
    modifier: Modifier = Modifier,
    sensorData: SensorData?,
    isDark: Boolean,
    sensitivity: Float = 4f
) {
    val roll = (sensorData?.roll ?: 0f) * sensitivity
    val pitch = (sensorData?.pitch ?: 0f) * sensitivity

    // تحديد الألوان الأساسية
    val baseColorsDark = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
    val reversedColorsDark = baseColorsDark.reversed()
    val baseColorsLight = listOf(Color(0xFFE0EAFC), Color(0xFFCFDEF3))
    val reversedColorsLight = baseColorsLight.reversed()

    // حساب النسبة بناءً على ميلان الجهاز (من -1 ل 1)
    val ratio = (pitch / 90f).coerceIn(-1f, 1f) // يمكنك تغيير 90f حسب الحساسية

    // تدرج سلس بين الألوان الأساسية والعكسية
    val colors = if (isDark) {
        List(baseColorsDark.size) { index ->
            lerp(baseColorsDark[index], reversedColorsDark[index], (ratio + 1) / 2)
        }
    } else {
        List(baseColorsLight.size) { index ->
            lerp(baseColorsLight[index], reversedColorsLight[index], (ratio + 1) / 2)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = colors.map { it.copy(alpha = 0.8f) }, // إضافة الشفافية
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            .blur(15.dp)
    )
}

@Composable
fun rememberSensorRotation(): State<SensorData?> {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val sensorDataState = remember { mutableStateOf<SensorData?>(null) }

    val smoothRoll = remember { mutableFloatStateOf(0f) }
    val smoothPitch = remember { mutableFloatStateOf(0f) }

    DisposableEffect(sensorManager) {
        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)

                    val pitch = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
                    val roll = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()

                    smoothRoll.floatValue = smoothRoll.floatValue * 0.8f + roll * 0.2f
                    smoothPitch.floatValue = smoothPitch.floatValue * 0.8f + pitch * 0.2f

                    sensorDataState.value = SensorData(roll = smoothRoll.floatValue, pitch = smoothPitch.floatValue)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return sensorDataState
}

data class SensorData(
    val roll: Float,
    val pitch: Float
)