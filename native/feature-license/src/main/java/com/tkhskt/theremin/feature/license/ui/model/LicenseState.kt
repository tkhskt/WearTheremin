package com.tkhskt.theremin.feature.license.ui.model

import com.tkhskt.theremin.domain.artifacts.valueobject.Artifact
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class LicenseState(
    val artifacts: List<Artifact>,
) {
    companion object {
        val INITIAL = LicenseState(
            artifacts = emptyList(),
        )
    }

    val uiState = LicenseUiState(
        artifactGroups = artifacts
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
            },
    )

    private fun String?.filterUrl(): String? {
        if (this?.contains("cs.android.com") == true) return null
        return this
    }

    private fun String.encode(): String {
        return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
    }
}
