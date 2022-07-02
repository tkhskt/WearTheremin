package com.tkhskt.theremin.feature.license.ui

import com.tkhskt.theremin.feature.license.ui.model.LicenseState
import com.tkhskt.theremin.feature.license.ui.model.LicenseUiState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                            url = it.url.encode(),
                        )
                    },
                    artifacts = artifacts.map {
                        LicenseUiState.ArtifactGroup.Artifact(
                            name = it.name,
                            version = it.version,
                            url = it.url.filterUrl()?.encode(),
                        )
                    },
                )
            }
        return LicenseUiState(
            artifactGroups = artifacts,
        )
    }

    private fun String?.filterUrl(): String? {
        if (this?.contains("cs.android.com") == true) return null
        return this
    }

    private fun String.encode(): String {
        return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
    }
}
