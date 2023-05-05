package com.tkhskt.theremin.domain.audio.usecase

import com.tkhskt.theremin.domain.audio.repository.MessageRepository
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface GetGravityUseCase {
    operator fun invoke(): Flow<Float>
}

class GetGravityUseCaseImpl(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val messageRepository: MessageRepository,
) : GetGravityUseCase {

    override fun invoke(): Flow<Float> {
        return messageRepository
            .getGravity()
            .flowOn(coroutineDispatcher)
    }
}
