package com.tkhskt.theremin.ui.model

sealed interface MainEvent {
    object Initialize : MainEvent
    object ClickStartButton : MainEvent
    object ClickStopButton : MainEvent
}
