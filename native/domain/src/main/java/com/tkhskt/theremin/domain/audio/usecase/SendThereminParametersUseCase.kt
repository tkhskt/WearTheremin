package com.tkhskt.theremin.domain.audio.usecase

import com.tkhskt.theremin.domain.audio.repository.AudioParameter
import com.tkhskt.theremin.domain.audio.repository.AudioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SendThereminParametersUseCase {
    suspend operator fun invoke(frequency: Float, volume: Float)
}

class SendThereminParametersUseCaseImpl(
    private val thereminRepository: AudioRepository,
) : SendThereminParametersUseCase {
    override suspend fun invoke(frequency: Float, volume: Float) {
        withContext(Dispatchers.IO) {
            thereminRepository.sendParameter(
                AudioParameter(frequency.toString(), volume.toString())
            )
        }
    }
}
