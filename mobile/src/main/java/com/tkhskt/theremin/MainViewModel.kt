package com.tkhskt.theremin

import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel :
    ViewModel(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {


    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _events = MutableStateFlow(Event(""))
    val events: StateFlow<Event> = _events


    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { dataEvent ->
            val uri = dataEvent.dataItem.uri
            when (uri.path) {
                AXIS_PATH -> {

                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        scope.launch {
            _events.value = Event(String(messageEvent.data))

        }
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {

    }

    companion object {
        private const val AXIS_PATH = "/axis"
    }
}

/**
 * A data holder describing a client event.
 */
data class Event(
    val text: String
)
