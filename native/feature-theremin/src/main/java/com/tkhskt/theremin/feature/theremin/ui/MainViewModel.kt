package com.tkhskt.theremin.feature.theremin.ui

import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.feature.theremin.domain.usecase.GetGravityUseCase
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import com.tkhskt.theremin.feature.theremin.ui.model.MainState
import com.tkhskt.theremin.feature.theremin.ui.model.MainUiState
import com.tkhskt.theremin.redux.ReduxViewModel
import com.tkhskt.theremin.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    override val uiState: StateFlow<MainUiState> = store.state
        .map(MainUiStateMapper::mapFromState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MainUiState.Initial,
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
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
