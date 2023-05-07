package com.tkhskt.theremin.wear.theremin.ui.model

sealed class MainAction {
    object StartSensor : MainAction()
    object ClickStartButton : MainAction()
    object ClickStopButton : MainAction()
    data class ChangeGravity(val gravity: Float) : MainAction()
}
