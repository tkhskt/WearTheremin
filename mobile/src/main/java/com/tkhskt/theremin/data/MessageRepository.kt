package com.tkhskt.theremin.data

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

interface MessageRepository {

}

class MessageRepositoryImpl : MessageRepository, MessageClient.OnMessageReceivedListener {

    private fun messageFlow(): Flow<MessageEvent> {
        return channelFlow {
            val listener = MessageClient.OnMessageReceivedListener {
                launch { send(it) }
            }
        }
    }

    override fun onMessageReceived(p0: MessageEvent) {

    }
}
