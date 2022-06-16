package com.tkhskt.theremin.feature.theremin.ui.model

import com.tkhskt.theremin.redux.Action

sealed interface MainAction : Action {
    object InitializeBle : MainAction
    data class ChangeGravity(val gravity: Float) : MainAction
    data class ChangeDistance(val distance: Float) : MainAction
    data class FrequencyChanged(val frequency: Float) : MainAction
    data class VolumeChanged(val volume: Float) : MainAction
    object ClickAppSoundButton : MainAction
    object ClickBrowserSoundButton : MainAction
}