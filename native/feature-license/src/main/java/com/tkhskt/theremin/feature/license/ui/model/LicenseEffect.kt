package com.tkhskt.theremin.feature.license.ui.model

import com.tkhskt.theremin.redux.SideEffect

sealed interface LicenseEffect : SideEffect {
    data class TransitToWebView(val url: String) : LicenseEffect
}
