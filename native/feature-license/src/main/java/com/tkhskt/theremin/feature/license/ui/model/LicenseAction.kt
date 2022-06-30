package com.tkhskt.theremin.feature.license.ui.model

import com.tkhskt.theremin.redux.Action

sealed interface LicenseAction : Action {
    object Initialize : LicenseAction
    data class ClickLicenseItem(
        val url: String,
    ) : LicenseAction

    data class ClickArtifactItem(
        val url: String,
    ) : LicenseAction
}
