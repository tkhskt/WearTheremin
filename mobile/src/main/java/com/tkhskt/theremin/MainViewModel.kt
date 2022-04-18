package com.tkhskt.theremin

import androidx.lifecycle.ViewModel
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
    MessageClient.OnMessageReceivedListener {


    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _events = MutableStateFlow(Event(""))
    val events: StateFlow<Event> = _events

    override fun onMessageReceived(messageEvent: MessageEvent) {
        scope.launch {
            _events.value = Event(String(messageEvent.data))
        }
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
