package com.tkhskt.theremin.feature.license.ui.model

import com.tkhskt.theremin.redux.UiState

data class LicenseUiState(
    val artifactGroups: List<ArtifactGroup>,
) : UiState {

    data class ArtifactGroup(
        val license: License,
        val artifacts: List<Artifact>,
    ) {
        data class License(
            val name: String,
            val url: String,
        )

        data class Artifact(
            val name: String,
            val version: String,
            val url: String?,
        )
    }

    companion object {
        val Initial = LicenseUiState(
            artifactGroups = emptyList(),
        )
    }
}
