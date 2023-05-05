package com.tkhskt.theremin.infra.message.repositoryimpl

import com.google.android.gms.wearable.MessageClient
import com.tkhskt.theremin.domain.audio.repository.MessageRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import timber.log.Timber
import java.lang.Exception

class MessageRepositoryImpl(
    private val messageClient: MessageClient,
) : MessageRepository {

    override fun getGravity() = channelFlow {
        val listener = MessageClient.OnMessageReceivedListener {
            try {
                trySend(String(it.data).toFloat())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        messageClient.addListener(listener)
        awaitClose {
            messageClient.removeListener(listener)
        }
    }
}
