package com.tkhskt.theremin.feature.theremin.ui.middleware

import com.tkhskt.theremin.domain.SendGravityUseCase
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import com.tkhskt.theremin.feature.theremin.ui.model.MainState
import kotlinx.coroutines.flow.SharedFlow

class NetworkMiddleware(
    private val sendGravityUseCase: SendGravityUseCase,
) : Middleware<MainAction, MainState, MainEffect> {

    override val sideEffect: SharedFlow<MainEffect>? = null

    override suspend fun dispatchBeforeReduce(action: MainAction, state: MainState): MainAction {
        return when (action) {
            is MainAction.ChangeGravity -> {
                if (state.started) {
                    sendGravityUseCase(action.gravity)
                }
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
