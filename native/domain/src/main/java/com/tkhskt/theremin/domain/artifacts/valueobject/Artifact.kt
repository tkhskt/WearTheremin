package com.tkhskt.theremin.domain.artifacts.valueobject

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
