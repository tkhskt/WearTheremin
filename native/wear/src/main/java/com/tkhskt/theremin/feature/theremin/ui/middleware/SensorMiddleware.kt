package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import com.tkhskt.theremin.feature.theremin.ui.model.MainState
import com.tkhskt.theremin.redux.Middleware
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SensorMiddleware : Middleware<MainAction, MainState, MainEffect> {

    private val _sideEffect = MutableSharedFlow<MainEffect>()
    override val sideEffect: SharedFlow<MainEffect>
        get() = _sideEffect

    override suspend fun dispatchBeforeReduce(action: MainAction, state: MainState): MainAction {
        return when (action) {
            is MainAction.StartSensor -> {
                _sideEffect.emit(MainEffect.StartSensor)
                action
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
