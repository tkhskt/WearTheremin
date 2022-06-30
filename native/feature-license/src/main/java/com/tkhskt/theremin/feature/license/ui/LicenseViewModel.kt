package com.tkhskt.theremin.feature.license.ui

import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.feature.license.domain.GetArtifactsUseCase
import com.tkhskt.theremin.feature.license.ui.model.LicenseAction
import com.tkhskt.theremin.feature.license.ui.model.LicenseEffect
import com.tkhskt.theremin.feature.license.ui.model.LicenseState
import com.tkhskt.theremin.feature.license.ui.model.LicenseUiState
import com.tkhskt.theremin.redux.Reducer
import com.tkhskt.theremin.redux.ReduxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
    private val getArtifactsUseCase: GetArtifactsUseCase,
) : ReduxViewModel<LicenseAction, LicenseUiState, LicenseEffect>(), Reducer<LicenseAction, LicenseState> {

    private val _sideEffect = MutableSharedFlow<LicenseEffect>()
    override val sideEffect: SharedFlow<LicenseEffect> = _sideEffect

    private val _state = MutableStateFlow(LicenseState.INITIAL)
    override val uiState: StateFlow<LicenseUiState> = _state
        .map(LicenseUiStateMapper::mapFromState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LicenseUiState.Initial,
        )

    override fun dispatch(action: LicenseAction) {
        viewModelScope.launch {
            val newState = reduce(action, _state.value)
            _state.value = newState
        }
    }

    override suspend fun reduce(action: LicenseAction, state: LicenseState): LicenseState {
        return when (action) {
            is LicenseAction.Initialize -> {
                LicenseState(
                    artifacts = getArtifactsUseCase.invoke(),
                )
            }
            is LicenseAction.ClickLicenseItem -> {
                state
            }
            is LicenseAction.ClickArtifactItem -> {
                state
            }
        }
    }
}
