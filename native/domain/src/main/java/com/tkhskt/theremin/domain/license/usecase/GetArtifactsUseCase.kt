package com.tkhskt.theremin.domain.license.usecase

import com.tkhskt.theremin.domain.license.repository.ArtifactsRepository
import com.tkhskt.theremin.domain.license.valueobject.Artifact

interface GetArtifactsUseCase {
    class ArtifactNotFoundException : Exception()

    operator fun invoke(): List<Artifact>
}

class GetArtifactsUseCaseImpl(
    private val artifactsRepository: ArtifactsRepository,
) : GetArtifactsUseCase {

    override fun invoke(): List<Artifact> {
        val artifacts = artifactsRepository.getArtifacts() ?: throw GetArtifactsUseCase.ArtifactNotFoundException()

        return artifacts.mapNotNull { artifact ->
            if (artifact.spdxLicenses == null && artifact.unknownLicenses == null) return@mapNotNull null
            val license = if (artifact.spdxLicenses?.isNotEmpty() == true) {
                artifact.spdxLicenses.map {
                    Artifact.License(
                        name = it.name,
                        url = it.url,
                    )
                }
            } else if (artifact.unknownLicenses?.isNotEmpty() == true) {
                artifact.unknownLicenses.map {
                    Artifact.License(
                        name = it.name,
                        url = it.url,
                    )
                }
            } else {
                emptyList()
            }
            Artifact(
                name = artifact.name,
                url = artifact.scm?.url,
                licenses = license,
                version = artifact.version,
            )
        }
    }
}
