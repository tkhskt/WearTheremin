package com.tkhskt.theremin.feature.theremin.ui.model

import com.tkhskt.theremin.redux.Action

sealed interface ThereminAction : Action {
    object InitializeBle : ThereminAction
    data class ChangeGravity(val gravity: Float) : ThereminAction
    data class ChangeDistance(val distance: Float) : ThereminAction
    data class FrequencyChanged(val frequency: Float) : ThereminAction
    data class VolumeChanged(val volume: Float) : ThereminAction
    object ClickAppSoundButton : ThereminAction
    object ClickBrowserSoundButton : ThereminAction
}
