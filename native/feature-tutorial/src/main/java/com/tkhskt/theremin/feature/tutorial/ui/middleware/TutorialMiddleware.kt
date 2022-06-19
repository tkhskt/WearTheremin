package com.tkhskt.theremin.feature.tutorial.ui.middleware

import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialAction
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialEffect
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialState
import com.tkhskt.theremin.redux.Middleware
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class TutorialMiddleware : Middleware<TutorialAction, TutorialState, TutorialEffect> {

    private val _sideEffect = MutableSharedFlow<TutorialEffect>()
    override val sideEffect: SharedFlow<TutorialEffect> = _sideEffect.asSharedFlow()

    override suspend fun dispatchBeforeReduce(action: TutorialAction, state: TutorialState): TutorialAction {
        if (action is TutorialAction.ClickStepButton && state.currentStep == TutorialState.Step.PITCH) {
            _sideEffect.emit(TutorialEffect.TransitToTheremin)
        }
        return action
    }

    override suspend fun dispatchAfterReduce(action: TutorialAction, state: TutorialState): TutorialAction {
        return action
    }
}
