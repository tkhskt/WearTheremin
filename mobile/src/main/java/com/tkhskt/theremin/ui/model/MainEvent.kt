package com.tkhskt.theremin.ui.model

sealed interface MainEvent {
    object ClickStartWearableButton : MainEvent
    object ClickCameraButton : MainEvent
    object InitializeBle : MainEvent
    data class ChangeFrequency(val frequency: Float) : MainEvent
    data class ChangeVolume(val volume: Float) : MainEvent
}
