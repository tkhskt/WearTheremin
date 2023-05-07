package com.tkhskt.theremin.wear.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.wear.domain.SendGravityUseCase
import com.tkhskt.theremin.wear.theremin.ui.model.MainAction
import com.tkhskt.theremin.wear.theremin.ui.model.MainEffect
import com.tkhskt.theremin.wear.theremin.ui.model.MainState
import com.tkhskt.theremin.wear.theremin.ui.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sendGravityUseCase: SendGravityUseCase,
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<MainEffect>()
    val sideEffect: SharedFlow<MainEffect> = _sideEffect.asSharedFlow()

    private val state = MutableStateFlow(MainState.INITIAL)
    val uiState: StateFlow<MainUiState> = state.map {
        MainUiState(
            started = it.started,
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Initial,
        started = SharingStarted.Lazily,
    )

    fun dispatch(action: MainAction) {
        viewModelScope.launch {
            when (action) {
                is MainAction.StartSensor -> {
                    _sideEffect.emit(MainEffect.StartSensor)
                }

                is MainAction.ChangeGravity -> {
                    if (state.value.started) {
                        sendGravityUseCase(action.gravity)
                    }
                }

                is MainAction.ClickStartButton -> {
                    state.update { it.copy(started = true) }
                }

                is MainAction.ClickStopButton -> {
                    state.update { it.copy(started = false) }
                }
            }
        }
    }
}
