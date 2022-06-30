package com.tkhskt.theremin.feature.license.ui.model

import com.tkhskt.theremin.feature.license.domain.model.Artifact
import com.tkhskt.theremin.redux.State

data class LicenseState(
    val artifacts: List<Artifact>,
) : State {
    companion object {
        val INITIAL = LicenseState(
            artifacts = emptyList(),
        )
    }
}
