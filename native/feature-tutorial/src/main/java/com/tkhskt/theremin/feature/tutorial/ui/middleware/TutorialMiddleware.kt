package com.tkhskt.theremin.feature.tutorial.ui.middleware

import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialAction
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialEffect
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialState
import com.tkhskt.theremin.redux.Middleware
import com.tkhskt.theremin.redux.Store
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class TutorialMiddleware : Middleware<TutorialAction, TutorialState, TutorialEffect> {

    private val _sideEffect = MutableSharedFlow<TutorialEffect>()
    override val sideEffect: SharedFlow<TutorialEffect> = _sideEffect.asSharedFlow()

    override suspend fun dispatch(store: Store<TutorialAction, TutorialState, TutorialEffect>): (suspend (TutorialAction) -> Unit) -> suspend (TutorialAction) -> Unit {
        return { next ->
            { action ->
                if (action is TutorialAction.ClickStepButton && store.currentState.currentStep == TutorialState.Step.PITCH) {
                    _sideEffect.emit(TutorialEffect.TransitToTheremin)
                }
                next(action)
            }
        }
    }
}
