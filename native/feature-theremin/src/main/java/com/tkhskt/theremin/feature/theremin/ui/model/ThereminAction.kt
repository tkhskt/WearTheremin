package com.tkhskt.theremin.feature.theremin.ui.model

sealed class ThereminAction {
    data class ChangeGravity(val gravity: Float) : ThereminAction()
    data class ChangeDistance(val distance: Float) : ThereminAction()
    data class FrequencyChanged(val frequency: Float) : ThereminAction()
    data class VolumeChanged(val volume: Float) : ThereminAction()
    object ClickAppSoundButton : ThereminAction()
    object ClickLicenseButton : ThereminAction()

    object Shake : ThereminAction()
}
