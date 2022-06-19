package com.tkhskt.theremin.feature.tutorial.ui.model

import com.tkhskt.theremin.redux.Action

sealed interface TutorialAction : Action {
    object ClickStepButton : TutorialAction
}
