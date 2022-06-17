package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.feature.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminEffect
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminState
import com.tkhskt.theremin.redux.Middleware
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

    override suspend fun dispatchBeforeReduce(action: ThereminAction, state: ThereminState): ThereminAction {
        return when (action) {
            is ThereminAction.InitializeBle -> {
                thereminRepository.initialize()
                action
            }
            is ThereminAction.ChangeGravity -> {
                val frequency = calcFrequencyUseCase(action.gravity)
                sendThereminParametersUseCase(state.frequency, state.volume)
                ThereminAction.FrequencyChanged(frequency)
            }
            is ThereminAction.ChangeDistance -> {
                val volume = calcVolumeUseCase(action.distance)
                sendThereminParametersUseCase(state.frequency, volume)
                ThereminAction.VolumeChanged(volume)
            }
            else -> {
                action
            }
        }
    }

    override suspend fun dispatchAfterReduce(action: ThereminAction, state: ThereminState): ThereminAction {
        return when (action) {
            is ThereminAction.ClickAppSoundButton -> {
                if (state.appSoundEnabled) {
                    _sideEffect.emit(ThereminEffect.StartCamera)
                }
                action
            }
            else -> {
                action
            }
        }
    }
}
