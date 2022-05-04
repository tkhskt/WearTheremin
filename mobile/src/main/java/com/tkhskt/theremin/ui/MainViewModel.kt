package com.tkhskt.theremin.ui

import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.domain.usecase.GetGravityUseCase
import com.tkhskt.theremin.redux.ReduxViewModel
import com.tkhskt.theremin.redux.Store
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import com.tkhskt.theremin.ui.model.MainState
import com.tkhskt.theremin.ui.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val store: Store<MainAction, MainState, MainEffect>,
    private val getGravityUseCase: GetGravityUseCase,
) : ReduxViewModel<MainAction, MainUiState, MainEffect>() {

    override val sideEffect: SharedFlow<MainEffect> = store.sideEffect.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
    )

    override val uiState: StateFlow<MainUiState> = store.state.map { state ->
        MainUiState(
            frequency = state.frequency,
            volume = state.volume,
            lerpVolume = state.lerpVolume,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MainUiState.Initial,
    )

    init {
        viewModelScope.launch {
            getGravityUseCase().collect { gravity ->
                dispatch(MainAction.ChangeGravity(gravity))
            }
        }
    }

    override fun dispatch(action: MainAction) {
        viewModelScope.launch {
            store.dispatch(action)
        }
    }
}
