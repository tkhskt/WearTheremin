package com.tkhskt.theremin.domain.audio.repository

import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getGravity(): Flow<Float>
}
