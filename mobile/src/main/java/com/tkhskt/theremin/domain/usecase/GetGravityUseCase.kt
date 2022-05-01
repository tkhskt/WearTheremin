package com.tkhskt.theremin.domain.usecase

import com.tkhskt.theremin.data.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

interface GetGravityUseCase {
    operator fun invoke(): Flow<Float>
}

class GetGravityUseCaseImpl(
    private val messageRepository: MessageRepository,
) : GetGravityUseCase {

    override fun invoke(): Flow<Float> {
        return messageRepository.getGravity()
    }
}
