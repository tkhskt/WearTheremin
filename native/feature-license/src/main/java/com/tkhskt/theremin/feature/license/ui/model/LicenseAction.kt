package com.tkhskt.theremin.feature.license.ui.model

sealed class LicenseAction {
    object Initialize : LicenseAction()
    data class ClickLicenseItem(
        val url: String,
    ) : LicenseAction()

    data class ClickArtifactItem(
        val url: String,
    ) : LicenseAction()
}
