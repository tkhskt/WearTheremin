package com.tkhskt.theremin.feature.theremin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.domain.audio.repository.AudioRepository
import com.tkhskt.theremin.domain.audio.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.domain.audio.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.domain.audio.usecase.GetGravityUseCase
import com.tkhskt.theremin.domain.audio.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminEffect
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminState
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThereminViewModel @Inject constructor(
    private val getGravityUseCase: GetGravityUseCase,
    private val audioRepository: AudioRepository,
    private val sendThereminParametersUseCase: SendThereminParametersUseCase,
    private val calcFrequencyUseCase: CalcFrequencyUseCase,
    private val calcVolumeUseCase: CalcVolumeUseCase,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<ThereminEffect>()
    val sideEffect: SharedFlow<ThereminEffect> = _sideEffect.asSharedFlow()

    private val _state = MutableStateFlow(ThereminState.INITIAL)
    val uiState: StateFlow<ThereminUiState> = _state
        .map { it.uiState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ThereminUiState.Initial,
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getGravityUseCase().collect { gravity ->
                dispatch(ThereminAction.ChangeGravity(gravity))
            }
        }
    }

    fun dispatch(action: ThereminAction) {
        viewModelScope.launch {
            when (action) {
                is ThereminAction.InitializeBle -> {
//                    audioRepository.initialize()
//                    _state.update { it.copy(bluetoothInitialized = true) }
                }

                is ThereminAction.ChangeGravity -> {
                    val frequency = calcFrequencyUseCase(action.gravity)
                    sendThereminParametersUseCase(_state.value.frequency, _state.value.volume)
                    _state.update { it.copy(frequency = frequency) }
                }

                is ThereminAction.ChangeDistance -> {
                    val volume = calcVolumeUseCase(action.distance)
                    sendThereminParametersUseCase(_state.value.frequency, volume)
                    _state.update { it.copy(volume = volume) }
                }

                is ThereminAction.ClickLicenseButton -> {
                    _sideEffect.emit(ThereminEffect.NavigateToLicense)
                }

                is ThereminAction.ClickAppSoundButton -> {
                    val appSoundEnabled = _state.value.appSoundEnabled
                    _state.update { it.copy(appSoundEnabled = !appSoundEnabled) }
                    if (!appSoundEnabled) {
                        _sideEffect.emit(ThereminEffect.StartCamera)
                    }
                }

                is ThereminAction.ClickBrowserSoundButton -> {
                    _state.update { it.copy(browserSoundEnabled = !it.browserSoundEnabled) }
                }

                else -> {
                }
            }

        }
    }
}
