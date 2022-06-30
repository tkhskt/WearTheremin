package com.tkhskt.theremin.feature.license.domain.model

data class Artifact(
    val name: String,
    val url: String?,
    val licenses: List<License>,
    val version: String,
) {

    data class License(
        val name: String,
        val url: String,
    )
}
