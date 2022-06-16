package com.tkhskt.theremin.feature.theremin.domain.usecase

import com.tkhskt.theremin.feature.theremin.data.model.ThereminParameter
import com.tkhskt.theremin.feature.theremin.data.repository.ThereminRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SendThereminParametersUseCase {
    suspend operator fun invoke(frequency: Float, volume: Float)
}

class SendThereminParametersUseCaseImpl(
    private val thereminRepository: ThereminRepository,
) : SendThereminParametersUseCase {
    override suspend fun invoke(frequency: Float, volume: Float) {
        withContext(Dispatchers.IO) {
            thereminRepository.sendParameter(
                ThereminParameter(frequency.toString(), volume.toString())
            )
        }
    }
}
