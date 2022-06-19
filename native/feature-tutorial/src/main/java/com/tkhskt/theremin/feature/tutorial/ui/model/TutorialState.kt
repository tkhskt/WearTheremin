package com.tkhskt.theremin.feature.tutorial.ui.model

import com.tkhskt.theremin.redux.State

data class TutorialState(
    val currentStep: Step,
) : State {
    enum class Step {
        PREPARATION,
        VOLUME,
        PITCH,
    }

    companion object {
        val INITIAL = TutorialState(
            currentStep = Step.PREPARATION,
        )
    }
}
