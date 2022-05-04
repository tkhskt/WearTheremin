package com.tkhskt.theremin.ui.middleware

import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import com.tkhskt.theremin.ui.model.MainState
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
