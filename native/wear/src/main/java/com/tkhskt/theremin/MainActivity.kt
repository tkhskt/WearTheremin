package com.tkhskt.theremin

import android.content.Context
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_GRAVITY
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tkhskt.theremin.ui.MainScreen
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val sensor by lazy {
        sensorManager.getDefaultSensor(TYPE_GRAVITY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        collectEffect()
        setContentView(
            ComposeView(this).apply {
                setContent {
                    MainScreen(viewModel)
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.dispatch(MainAction.StartSensor)
    }

    private fun collectEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.sideEffect.collect {
                    if (it is MainEffect.StartSensor) {
                        startSensor()
                    }
                }
            }
        }
    }

    private suspend fun startSensor() {
        sensorEventFlow(sensor, sensorManager).collect {
            val y = it.values.getOrNull(1) ?: return@collect
            viewModel.dispatch(MainAction.ChangeGravity(y))
        }
    }

    private fun sensorEventFlow(
        sensor: Sensor,
        manager: SensorManager,
    ): Flow<SensorEvent> {
        return channelFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event?.sensor?.type == sensor.type) {
                        launch { send(event) }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // no-op
                }
            }
            try {
                manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST)
                awaitClose()
            } finally {
                manager.unregisterListener(listener, sensor)
            }
        }
    }
}
