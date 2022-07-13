package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import com.tkhskt.theremin.feature.theremin.ui.model.MainState
import com.tkhskt.theremin.redux.Dispatcher
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.redux.Store
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SensorMiddleware : Middleware<MainAction, MainState, MainEffect> {

    private val _sideEffect = MutableSharedFlow<MainEffect>()
    override val sideEffect: SharedFlow<MainEffect>
        get() = _sideEffect

    override suspend fun dispatch(store: Store<MainAction, MainState, MainEffect>): (Dispatcher<MainAction>) -> Dispatcher<MainAction> {
        return { next ->
            Dispatcher { action ->
                when (action) {
                    is MainAction.StartSensor -> {
                        _sideEffect.emit(MainEffect.StartSensor)
                    }
                    else -> {}
                }
                next.dispatch(action)
            }
        }
    }
}
