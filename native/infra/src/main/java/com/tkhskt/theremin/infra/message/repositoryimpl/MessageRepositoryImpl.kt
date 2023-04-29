package com.tkhskt.theremin.infra.message.repositoryimpl

import com.google.android.gms.wearable.MessageClient
import com.tkhskt.theremin.domain.audio.repository.MessageRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow

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
