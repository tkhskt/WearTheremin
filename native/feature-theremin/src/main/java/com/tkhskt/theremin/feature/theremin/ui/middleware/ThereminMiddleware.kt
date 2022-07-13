package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.feature.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminEffect
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminState
import com.tkhskt.theremin.redux.Dispatcher
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.redux.Store
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ThereminMiddleware(
    private val thereminRepository: ThereminRepository,
    private val sendThereminParametersUseCase: SendThereminParametersUseCase,
    private val calcFrequencyUseCase: CalcFrequencyUseCase,
    private val calcVolumeUseCase: CalcVolumeUseCase,
) : Middleware<ThereminAction, ThereminState, ThereminEffect> {

    private val _sideEffect = MutableSharedFlow<ThereminEffect>()
    override val sideEffect: SharedFlow<ThereminEffect> = _sideEffect

    override suspend fun dispatch(store: Store<ThereminAction, ThereminState, ThereminEffect>): (Dispatcher<ThereminAction>) -> Dispatcher<ThereminAction> {
        return { next ->
            Dispatcher { action ->
                val newAction = when (action) {
                    is ThereminAction.InitializeBle -> {
                        thereminRepository.initialize()
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
