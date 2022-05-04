package com.tkhskt.theremin.ui.middleware

import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.domain.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.domain.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.domain.usecase.GetVolumeUseCase
import com.tkhskt.theremin.domain.usecase.OpenWearAppUseCase
import com.tkhskt.theremin.domain.usecase.SaveVolumeUseCase
import com.tkhskt.theremin.domain.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import com.tkhskt.theremin.ui.model.MainState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

class MainMiddleware(
    private val thereminRepository: ThereminRepository,
    private val sendThereminParametersUseCase: SendThereminParametersUseCase,
    private val calcFrequencyUseCase: CalcFrequencyUseCase,
    private val calcVolumeUseCase: CalcVolumeUseCase,
    private val openWearAppUseCase: OpenWearAppUseCase,
    private val saveVolumeUseCase: SaveVolumeUseCase,
    private val getVolumeUseCase: GetVolumeUseCase,
) : Middleware<MainAction, MainState, MainEffect> {

    private val _sideEffect = MutableSharedFlow<MainEffect>()
    override val sideEffect: SharedFlow<MainEffect> = _sideEffect

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
                sendThereminParametersUseCase(state.frequency, state.volume)
                MainAction.FrequencyChanged(frequency)
            }
            is MainAction.ChangeDistance -> {
                val previousVolume = getVolumeUseCase()
                val newVolume = calcVolumeUseCase(action.distance)
                val lerpVolume = mutableListOf<Float>()
                (0 until 100).forEach { index ->
                    if (index == 0) {
                        lerpVolume.add(previousVolume)
                        return@forEach
                    }
                    if (index == 99) {
                        lerpVolume.add(newVolume)
                        return@forEach
                    }
                    val next = lerpVolume[index - 1] + ((newVolume - previousVolume) / 100f)
                    lerpVolume.add(next)
                }
                saveVolumeUseCase(newVolume)
                sendThereminParametersUseCase(state.frequency, newVolume)
                MainAction.VolumeChanged(newVolume, lerpVolume)
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
