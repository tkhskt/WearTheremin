package com.tkhskt.theremin.domain

import com.tkhskt.theremin.data.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

interface GetGravityUseCase {
    operator fun invoke(): Flow<String>
}

class GetGravityUseCaseImpl(
    private val messageRepository: MessageRepository,
) : GetGravityUseCase {

    override fun invoke(): Flow<String> {
        return messageRepository.getGravity()
    }
}
