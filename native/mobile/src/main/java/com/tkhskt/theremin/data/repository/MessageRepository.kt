package com.tkhskt.theremin.data.repository

import com.google.android.gms.wearable.MessageClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

interface MessageRepository {
    fun getGravity(): Flow<Float>
}

class MessageRepositoryImpl(
    private val messageClient: MessageClient,
) : MessageRepository {

    override fun getGravity() = channelFlow {
        val listener = MessageClient.OnMessageReceivedListener {
            trySend(String(it.data).toFloat())
        }
        messageClient.addListener(listener)
        awaitClose {
            messageClient.removeListener(listener)
        }
    }
}
