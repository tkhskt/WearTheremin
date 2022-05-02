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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val store: Store<MainAction, MainState, MainEffect>,
    private val getGravityUseCase: GetGravityUseCase,
) : ReduxViewModel<MainAction, MainUiState, MainEffect>() {

    val onChangeDistanceListener = { distance: Float ->
        Timber.d(distance.toString())
        dispatch(MainAction.ChangeDistance(distance))
    }

    override val sideEffect: SharedFlow<MainEffect>
        get() = store.sideEffect.shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
        )

    override val uiState: StateFlow<MainUiState>
        get() = store.state.map { state ->
            MainUiState(
                frequency = state.frequency,
                volume = state.volume,
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
