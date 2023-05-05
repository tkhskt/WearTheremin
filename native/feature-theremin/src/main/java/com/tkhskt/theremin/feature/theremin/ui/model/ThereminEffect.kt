package com.tkhskt.theremin.feature.theremin.ui.model

sealed class ThereminEffect {
    object StartCamera : ThereminEffect()
    object NavigateToLicense : ThereminEffect()
}
