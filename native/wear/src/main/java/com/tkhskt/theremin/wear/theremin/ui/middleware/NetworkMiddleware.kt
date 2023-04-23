package com.tkhskt.theremin.wear.theremin.ui.middleware

import com.tkhskt.theremin.wear.domain.SendGravityUseCase
import com.tkhskt.theremin.wear.theremin.ui.model.MainAction
import com.tkhskt.theremin.wear.theremin.ui.model.MainEffect
import com.tkhskt.theremin.wear.theremin.ui.model.MainState
import com.tkhskt.theremin.redux.Dispatcher
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.redux.Store
import kotlinx.coroutines.flow.SharedFlow

class NetworkMiddleware(
    private val sendGravityUseCase: SendGravityUseCase,
) : Middleware<MainAction, MainState, MainEffect> {

    override val sideEffect: SharedFlow<MainEffect>? = null

    override suspend fun dispatch(store: Store<MainAction, MainState, MainEffect>): (Dispatcher<MainAction>) -> Dispatcher<MainAction> {
        return { next ->
            Dispatcher { action ->
                when (action) {
                    is MainAction.ChangeGravity -> {
                        if (store.currentState.started) {
                            sendGravityUseCase(action.gravity)
                        }
                    }
                    else -> {}
                }
                next.dispatch(action)
            }
        }
    }
}
