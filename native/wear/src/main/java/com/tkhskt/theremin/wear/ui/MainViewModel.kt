package com.tkhskt.theremin.wear.ui

import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.wear.theremin.ui.model.MainAction
import com.tkhskt.theremin.wear.theremin.ui.model.MainEffect
import com.tkhskt.theremin.wear.theremin.ui.model.MainState
import com.tkhskt.theremin.wear.theremin.ui.model.MainUiState
import com.tkhskt.theremin.redux.ReduxViewModel
import com.tkhskt.theremin.redux.Store
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
) : ReduxViewModel<MainAction, MainUiState, MainEffect>() {

    override val sideEffect: SharedFlow<MainEffect> = store.sideEffect.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
    )

    override val uiState: StateFlow<MainUiState> = store.state.map {
        MainUiState(
            started = it.started,
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainUiState.Initial,
        started = SharingStarted.Lazily,
    )

    override fun dispatch(action: MainAction) {
        viewModelScope.launch {
            val b: Unit = store.dispatch(action)
        }
    }
}
