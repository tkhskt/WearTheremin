package com.tkhskt.theremin.wear.theremin.data.repository

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import kotlinx.coroutines.tasks.await

interface ThereminRepository {
    suspend fun sendGravity(y: Float)
}

class ThereminRepositoryImpl(
    private val messageClient: MessageClient,
    private val nodeClient: NodeClient,
) : ThereminRepository {

    override suspend fun sendGravity(y: Float) {
        val nodes = nodeClient.connectedNodes.await()
        nodes.forEach { node ->
            messageClient.sendMessage(
                node.id,
                GRAVITY_PATH,
                y.toString().toByteArray()
            ).await()
        }
    }

    companion object {
        private const val GRAVITY_PATH = "/gravity"
    }
}
