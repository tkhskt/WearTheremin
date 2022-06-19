package com.tkhskt.theremin.feature.tutorial.ui

import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialAction
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialEffect
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialState
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialUiState
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
class TutorialViewModel @Inject constructor(
    private val store: Store<TutorialAction, TutorialState, TutorialEffect>,
) : ReduxViewModel<TutorialAction, TutorialUiState, TutorialEffect>() {

    override val sideEffect: SharedFlow<TutorialEffect> = store.sideEffect.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
    )

    override val uiState: StateFlow<TutorialUiState> = store.state
        .map(TutorialUiStateMapper::mapFromState)
        .stateIn(
            scope = viewModelScope,
            initialValue = TutorialUiState.Initial,
            started = SharingStarted.Lazily,
        )

    override fun dispatch(action: TutorialAction) {
        viewModelScope.launch {
            store.dispatch(action)
        }
    }
}
