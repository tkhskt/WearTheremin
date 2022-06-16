package com.tkhskt.theremin.feature.theremin.ui.model

import com.tkhskt.theremin.redux.Action

sealed interface MainAction : Action {
    object StartSensor : MainAction
    object ClickStartButton : MainAction
    object ClickStopButton : MainAction
    data class ChangeGravity(val gravity: Float) : MainAction
}
