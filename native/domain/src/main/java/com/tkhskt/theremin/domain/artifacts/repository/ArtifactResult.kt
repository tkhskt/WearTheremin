package com.tkhskt.theremin.domain.artifacts.repository

data class ArtifactResult(
    val artifactId: String,
    val groupId: String,
    val name: String,
    val scm: Scm?,
    val spdxLicenses: List<SpdxLicense>?,
    val unknownLicenses: List<UnknownLicense>?,
    val version: String,
) {
    data class Scm(
        val url: String,
    )

    data class SpdxLicense(
        val identifier: String,
        val name: String,
        val url: String,
    )

    data class UnknownLicense(
        val name: String,
        val url: String,
    )
}
