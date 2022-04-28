package com.tkhskt.theremin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.data.model.ThereminParameter
import com.tkhskt.theremin.ui.model.MainEvent
import com.tkhskt.theremin.ui.model.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val thereminRepository: ThereminRepository,
) : ViewModel(), MessageClient.OnMessageReceivedListener {

    private val _state = MutableStateFlow(MainState.Empty)
    val state: StateFlow<MainState>
        get() = _state

    private val _events = MutableSharedFlow<MainEvent>()
    val events: SharedFlow<MainEvent>
        get() = _events


    init {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is MainEvent.InitializeBle -> {
                        thereminRepository.initialize()
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                acceleration = String(messageEvent.data),
            )
            thereminRepository.sendParameter(ThereminParameter(String(messageEvent.data), ""))
        }
    }

    fun dispatchEvent(event: MainEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
