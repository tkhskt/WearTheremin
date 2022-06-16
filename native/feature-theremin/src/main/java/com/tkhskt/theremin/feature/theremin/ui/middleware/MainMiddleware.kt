package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.feature.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import com.tkhskt.theremin.feature.theremin.ui.model.MainState
import com.tkhskt.theremin.redux.Middleware
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class MainMiddleware(
    private val thereminRepository: ThereminRepository,
    private val sendThereminParametersUseCase: SendThereminParametersUseCase,
    private val calcFrequencyUseCase: CalcFrequencyUseCase,
    private val calcVolumeUseCase: CalcVolumeUseCase,
) : Middleware<MainAction, MainState, MainEffect> {

    private val _sideEffect = MutableSharedFlow<MainEffect>()
    override val sideEffect: SharedFlow<MainEffect> = _sideEffect

    override suspend fun dispatchBeforeReduce(action: MainAction, state: MainState): MainAction {
        return when (action) {
            is MainAction.InitializeBle -> {
                thereminRepository.initialize()
                action
            }
            is MainAction.ChangeGravity -> {
                val frequency = calcFrequencyUseCase(action.gravity)
                sendThereminParametersUseCase(state.frequency, state.volume)
                MainAction.FrequencyChanged(frequency)
            }
            is MainAction.ChangeDistance -> {
                val volume = calcVolumeUseCase(action.distance)
                sendThereminParametersUseCase(state.frequency, volume)
                MainAction.VolumeChanged(volume)
            }
            else -> {
                action
            }
        }
    }

    override suspend fun dispatchAfterReduce(action: MainAction, state: MainState): MainAction {
        return when (action) {
            is MainAction.ClickAppSoundButton -> {
                if (state.appSoundEnabled) {
                    _sideEffect.emit(MainEffect.StartCamera)
                }
                action
            }
            else -> {
                action
            }
        }
    }
}
