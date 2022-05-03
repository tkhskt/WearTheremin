package com.tkhskt.theremin.domain.usecase

import com.tkhskt.theremin.data.model.ThereminParameter
import com.tkhskt.theremin.data.repository.ThereminRepository

interface SendThereminParametersUseCase {
    suspend operator fun invoke(frequency: Float, volume: Float)
}

class SendThereminParametersUseCaseImpl(
    private val thereminRepository: ThereminRepository
) : SendThereminParametersUseCase {
    override suspend fun invoke(frequency: Float, volume: Float) {
        thereminRepository.sendParameter(
            ThereminParameter(frequency.toString(), volume.toString())
        )
    }
}
