package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.domain.SendGravityUseCase
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import com.tkhskt.theremin.feature.theremin.ui.model.MainState
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.redux.Store
import kotlinx.coroutines.flow.SharedFlow

class NetworkMiddleware(
    private val sendGravityUseCase: SendGravityUseCase,
) : Middleware<MainAction, MainState, MainEffect> {

    override val sideEffect: SharedFlow<MainEffect>? = null

    override suspend fun dispatch(store: Store<MainAction, MainState, MainEffect>): (suspend (MainAction) -> Unit) -> suspend (MainAction) -> Unit {
        return { next ->
            { action ->
                when (action) {
                    is MainAction.ChangeGravity -> {
                        if (store.currentState.started) {
                            sendGravityUseCase(action.gravity)
                        }
                    }
                    else -> {}
                }
                next(action)
            }
        }
    }
}
