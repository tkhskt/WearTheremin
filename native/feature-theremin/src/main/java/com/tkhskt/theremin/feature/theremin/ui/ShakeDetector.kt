package com.tkhskt.theremin.feature.theremin.ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.sqrt

class ShakeDetector(
    private val sensorManager: SensorManager,
    private val onShake: () -> Unit,
) : DefaultLifecycleObserver {

    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > 6) {
                onShake()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        sensorManager.registerListener(
            sensorListener,
            sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL,
        )

        acceleration = 4f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    override fun onResume(owner: LifecycleOwner) {
        sensorManager.registerListener(
            sensorListener,
            sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER,
            ),
            SensorManager.SENSOR_DELAY_NORMAL,
        )
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        sensorManager.unregisterListener(sensorListener)
        super.onPause(owner)
    }
}
