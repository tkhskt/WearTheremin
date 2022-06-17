package com.tkhskt.theremin.feature.theremin.ui

import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.feature.theremin.domain.usecase.GetGravityUseCase
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminEffect
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminState
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminUiState
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
class ThereminViewModel @Inject constructor(
    private val store: Store<ThereminAction, ThereminState, ThereminEffect>,
    private val getGravityUseCase: GetGravityUseCase,
) : ReduxViewModel<ThereminAction, ThereminUiState, ThereminEffect>() {

    override val sideEffect: SharedFlow<ThereminEffect> = store.sideEffect.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
    )

    override val uiState: StateFlow<ThereminUiState> = store.state
        .map(MainUiStateMapper::mapFromState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ThereminUiState.Initial,
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getGravityUseCase().collect { gravity ->
                dispatch(ThereminAction.ChangeGravity(gravity))
            }
        }
    }

    override fun dispatch(action: ThereminAction) {
        viewModelScope.launch {
            store.dispatch(action)
        }
    }
}
