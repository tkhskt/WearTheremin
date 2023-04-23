package com.tkhskt.theremin.feature.license.ui.model

import com.tkhskt.theremin.redux.State

data class LicenseState(
    val artifacts: List<com.tkhskt.theremin.domain.artifacts.valueobject.Artifact>,
) : State {
    companion object {
        val INITIAL = LicenseState(
            artifacts = emptyList(),
        )
    }
}
