package com.tkhskt.theremin.ui.middleware

import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.domain.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.domain.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.domain.usecase.OpenWearAppUseCase
import com.tkhskt.theremin.domain.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import com.tkhskt.theremin.ui.model.MainState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class MainMiddleware(
    private val thereminRepository: ThereminRepository,
    private val sendThereminParametersUseCase: SendThereminParametersUseCase,
    private val calcFrequencyUseCase: CalcFrequencyUseCase,
    private val calcVolumeUseCase: CalcVolumeUseCase,
    private val openWearAppUseCase: OpenWearAppUseCase,
) : Middleware<MainAction, MainState, MainEffect> {

    private val _sideEffect = MutableSharedFlow<MainEffect>()
    override val sideEffect: SharedFlow<MainEffect>
        get() = _sideEffect

    override suspend fun dispatchBeforeReduce(action: MainAction, state: MainState): MainAction {
        return when (action) {
            is MainAction.InitializeBle -> {
                thereminRepository.initialize()
                action
            }
            is MainAction.ClickStartWearableButton -> {
                openWearAppUseCase()
                action
            }
            is MainAction.ClickCameraButton -> {
                _sideEffect.emit(MainEffect.StartCamera)
                action
            }
            is MainAction.ChangeGravity -> {
                val frequency = calcFrequencyUseCase(action.gravity)
                sendThereminParametersUseCase(state.frequency.toString(), state.volume.toString())
                MainAction.FrequencyChanged(frequency)
            }
            is MainAction.ChangeDistance -> {
                val volume = calcVolumeUseCase(action.distance)
                sendThereminParametersUseCase(state.frequency.toString(), volume.toString())
                MainAction.VolumeChanged(volume)
            }
            else -> {
                action
            }
        }
    }

    override suspend fun dispatchAfterReduce(action: MainAction, state: MainState): MainAction {
        return action
    }
}
