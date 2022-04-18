package com.tkhskt.theremin.ui.model

sealed class MainEvent {
    object Initialize : MainEvent()
    object ClickStartButton : MainEvent()
    object ClickStopButton : MainEvent()
}
