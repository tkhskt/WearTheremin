package com.tkhskt.theremin.feature.license.ui.model

sealed class LicenseEffect {
    data class TransitToWebView(val url: String) : LicenseEffect()
}
