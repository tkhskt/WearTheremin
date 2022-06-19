package com.tkhskt.theremin.feature.tutorial.ui.reducer

import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialAction
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialState
import com.tkhskt.theremin.redux.Reducer
import kotlinx.coroutines.delay

class TutorialReducer : Reducer<TutorialAction, TutorialState> {
    override suspend fun reduce(action: TutorialAction, state: TutorialState): TutorialState {
        return when (action) {
            is TutorialAction.ClickStepButton -> {
                val nextStep = when (state.currentStep) {
                    TutorialState.Step.PREPARATION -> {
                        delay(DELAY_FOR_ANIMATION)
                        TutorialState.Step.VOLUME
                    }
                    TutorialState.Step.VOLUME -> {
                        delay(DELAY_FOR_ANIMATION)
                        TutorialState.Step.PITCH
                    }
                    TutorialState.Step.PITCH -> TutorialState.Step.PITCH
                }
                state.copy(
                    currentStep = nextStep,
                )
            }
        }
    }

    companion object {
        private const val DELAY_FOR_ANIMATION = 500L
    }
}
