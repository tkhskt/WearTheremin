package com.tkhskt.theremin.feature.license.ui

import com.tkhskt.theremin.feature.license.ui.model.LicenseState
import com.tkhskt.theremin.feature.license.ui.model.LicenseUiState

object LicenseUiStateMapper {
    fun mapFromState(state: LicenseState): LicenseUiState {
        val artifacts = state.artifacts
            .groupBy { it.licenses }
            .filter { (license, _) -> license.isNotEmpty() }
            .map { (license, artifacts) ->
                LicenseUiState.ArtifactGroup(
                    license = license.first().let {
                        LicenseUiState.ArtifactGroup.License(
                            name = it.name,
                            url = it.url,
                        )
                    },
                    artifacts = artifacts.map {
                        LicenseUiState.ArtifactGroup.Artifact(
                            name = it.name,
                            version = it.version,
                            url = it.url,
                        )
                    },
                )
            }
        return LicenseUiState(
            artifactGroups = artifacts,
        )
    }
}
