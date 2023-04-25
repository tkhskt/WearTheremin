package com.tkhskt.theremin.domain.artifacts.repository

interface ArtifactsRepository {
    fun getArtifacts(): List<ArtifactResult>?
}
