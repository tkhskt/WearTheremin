package com.tkhskt.theremin.domain.usecase

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

interface OpenWearAppUseCase {
    suspend operator fun invoke()
}

class OpenWearAppUseCaseImpl(
    private val messageClient: MessageClient,
    private val nodeClient: NodeClient,
) : OpenWearAppUseCase {

    override suspend fun invoke() {
        withContext(Dispatchers.IO) {
            try {
                val nodes = nodeClient.connectedNodes.await()
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(node.id, START_ACTIVITY_PATH, byteArrayOf())
                            .await()
                    }
                }.awaitAll()
                Timber.d("Starting activity requests sent successfully")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Timber.d("Starting activity failed: $exception")
            }
        }
    }

    companion object {
        private const val START_ACTIVITY_PATH = "/start-activity"
    }
}
