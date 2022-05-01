package com.tkhskt.theremin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.domain.CalcFrequencyUseCase
import com.tkhskt.theremin.domain.CalcVolumeUseCase
import com.tkhskt.theremin.domain.GetGravityUseCase
import com.tkhskt.theremin.domain.OpenWearAppUseCase
import com.tkhskt.theremin.domain.SendThereminParametersUseCase
import com.tkhskt.theremin.ui.model.MainEvent
import com.tkhskt.theremin.ui.model.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val thereminRepository: ThereminRepository,
    private val sendThereminParametersUseCase: SendThereminParametersUseCase,
    private val getGravityUseCase: GetGravityUseCase,
    private val calcFrequencyUseCase: CalcFrequencyUseCase,
    private val calcVolumeUseCase: CalcVolumeUseCase,
    private val openWearAppUseCase: OpenWearAppUseCase,
) : ViewModel() {

    val onChangeDistanceListener = { distance: Float ->
        Timber.d(distance.toString())
        val volume = calcVolumeUseCase(distance)
        dispatchEvent(MainEvent.ChangeVolume(volume))
    }

    private val _state = MutableStateFlow(MainState.Empty)
    val state: StateFlow<MainState>
        get() = _state

    private val _events = MutableSharedFlow<MainEvent>()
    val events: SharedFlow<MainEvent>
        get() = _events

    init {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is MainEvent.InitializeBle -> {
                        thereminRepository.initialize()
                    }
                    is MainEvent.ClickStartWearableButton -> {
                        openWearAppUseCase()
                    }
                    is MainEvent.ChangeFrequency -> {
                        _state.value = state.value.copy(
                            frequency = it.frequency.toString()
                        )
                        sendThereminParametersUseCase(it.frequency.toString(), "")
                    }
                    is MainEvent.ChangeVolume -> {
                        _state.value = state.value.copy(
                            volume = it.volume.toString()
                        )
                        sendThereminParametersUseCase("", it.volume.toString())
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        }
        viewModelScope.launch {
            getGravityUseCase().collect {
                val frequency = calcFrequencyUseCase(it)
                dispatchEvent(MainEvent.ChangeFrequency(frequency))
            }
        }
    }

    fun dispatchEvent(event: MainEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
