package com.tkhskt.theremin.domain.usecase

import com.tkhskt.theremin.data.model.ThereminParameter
import com.tkhskt.theremin.data.repository.ThereminRepository

interface SendThereminParametersUseCase {
    suspend operator fun invoke(frequency: String, volume: String)
}

class SendThereminParametersUseCaseImpl(
    private val thereminRepository: ThereminRepository
) : SendThereminParametersUseCase {
    override suspend fun invoke(frequency: String, volume: String) {
        thereminRepository.sendParameter(
            ThereminParameter(frequency, volume)
        )
    }
}
