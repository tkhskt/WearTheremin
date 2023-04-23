package com.tkhskt.theremin.feature.license.ui.model

import com.tkhskt.theremin.domain.license.valueobject.Artifact
import com.tkhskt.theremin.redux.State

data class LicenseState(
    val artifacts: List<com.tkhskt.theremin.domain.license.valueobject.Artifact>,
) : State {
    companion object {
        val INITIAL = LicenseState(
            artifacts = emptyList(),
        )
    }
}
