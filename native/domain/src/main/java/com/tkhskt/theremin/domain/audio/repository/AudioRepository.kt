package com.tkhskt.theremin.domain.audio.repository

interface AudioRepository {
    suspend fun initialize()
    suspend fun sendParameter(parameter: AudioParameter)
}
