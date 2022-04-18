package com.tkhskt.theremin.ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.data.ThereminRepository
import com.tkhskt.theremin.ui.model.MainEvent
import com.tkhskt.theremin.ui.model.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val thereminRepository: ThereminRepository,
    private val sensorManager: SensorManager,
    private val sensor: Sensor,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState.Empty)
    val state: StateFlow<MainState>
        get() = _state

    private val event = MutableSharedFlow<MainEvent>()

    init {
        viewModelScope.launch {
            event.collect {
                when (it) {
                    is MainEvent.Initialize -> {
                        startSensor()
                    }
                    is MainEvent.ClickStartButton -> {
                        _state.value = MainState(started = true)
                    }
                    is MainEvent.ClickStopButton -> {
                        _state.value = MainState(started = false)
                    }
                }
            }
        }
    }

    fun dispatchEvent(mainEvent: MainEvent) {
        viewModelScope.launch {
            event.emit(mainEvent)
        }
    }

    private fun startSensor() {
        viewModelScope.launch {
            sensorEventFlow(sensor, sensorManager).collect {
                val x = it.values.getOrNull(0) ?: return@collect
                thereminRepository.sendAcceleration(x)
            }
        }
    }

    private fun sensorEventFlow(
        sensor: Sensor,
        manager: SensorManager,
    ): Flow<SensorEvent> {
        return channelFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event?.sensor?.type == sensor.type && state.value.started) {
                        launch { send(event) }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // no-op
                }
            }
            try {
                manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                awaitClose()
            } finally {
                manager.unregisterListener(listener, sensor)
            }
        }
    }

}
