package com.tkhskt.theremin.ui.model

sealed interface MainEvent {
    object ClickStartWearableButton : MainEvent
    object ClickCameraButton : MainEvent
    object InitializeBle : MainEvent
}
