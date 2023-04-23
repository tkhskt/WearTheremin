package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.domain.audio.repository.AudioRepository
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminEffect
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminState
import com.tkhskt.theremin.redux.Dispatcher
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.redux.Store
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ThereminMiddleware(
    private val audioRepository: AudioRepository,
    private val sendThereminParametersUseCase: com.tkhskt.theremin.domain.audio.usecase.SendThereminParametersUseCase,
    private val calcFrequencyUseCase: com.tkhskt.theremin.domain.audio.usecase.CalcFrequencyUseCase,
    private val calcVolumeUseCase: com.tkhskt.theremin.domain.audio.usecase.CalcVolumeUseCase,
) : Middleware<ThereminAction, ThereminState, ThereminEffect> {

    private val _sideEffect = MutableSharedFlow<ThereminEffect>()
    override val sideEffect: SharedFlow<ThereminEffect> = _sideEffect

    override suspend fun dispatch(store: Store<ThereminAction, ThereminState, ThereminEffect>): (Dispatcher<ThereminAction>) -> Dispatcher<ThereminAction> {
        return { next ->
            Dispatcher { action ->
                val newAction = when (action) {
                    is ThereminAction.InitializeBle -> {
                        audioRepository.initialize()
                        action
                    }
                    is ThereminAction.ChangeGravity -> {
                        val frequency = calcFrequencyUseCase(action.gravity)
                        sendThereminParametersUseCase(store.currentState.frequency, store.currentState.volume)
                        ThereminAction.FrequencyChanged(frequency)
                    }
                    is ThereminAction.ChangeDistance -> {
                        val volume = calcVolumeUseCase(action.distance)
                        sendThereminParametersUseCase(store.currentState.frequency, volume)
                        ThereminAction.VolumeChanged(volume)
                    }
                    is ThereminAction.ClickLicenseButton -> {
                        _sideEffect.emit(ThereminEffect.NavigateToLicense)
                        action
                    }
                    else -> {
                        action
                    }
                }
                next.dispatch(newAction)
                if (store.currentState.appSoundEnabled) {
                    _sideEffect.emit(ThereminEffect.StartCamera)
                }
            }
        }
    }
}
