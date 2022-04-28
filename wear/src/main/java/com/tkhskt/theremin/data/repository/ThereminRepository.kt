package com.tkhskt.theremin.data.repository

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface ThereminRepository {
    suspend fun sendAcceleration(x: Float)
}

class ThereminRepositoryImpl(
    private val messageClient: MessageClient,
    private val nodeClient: NodeClient,
) : ThereminRepository {

    override suspend fun sendAcceleration(x: Float) {
        val nodes = nodeClient.connectedNodes.await()

       withContext(Dispatchers.IO) {
           nodes.forEach { node ->
               messageClient.sendMessage(
                   node.id,
                   ACCELERATION_PATH,
                   x.toString().toByteArray()
               ).await()
           }
       }
    }

    companion object {
        private const val ACCELERATION_PATH = "/acceleration"
    }
}
