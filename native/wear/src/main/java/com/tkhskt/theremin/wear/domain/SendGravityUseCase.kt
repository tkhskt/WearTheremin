package com.tkhskt.theremin.wear.domain

import com.tkhskt.theremin.wear.theremin.data.repository.ThereminRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SendGravityUseCase {
    suspend operator fun invoke(gravity: Float)
}

class SendGravityUseCaseImpl(
    private val thereminRepository: ThereminRepository,
) : SendGravityUseCase {

    override suspend fun invoke(gravity: Float) {
        withContext(Dispatchers.IO) {
            thereminRepository.sendGravity(gravity)
        }
    }
}
