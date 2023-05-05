package com.tkhskt.theremin.feature.license.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.domain.artifacts.usecase.GetArtifactsUseCase
import com.tkhskt.theremin.feature.license.ui.model.LicenseAction
import com.tkhskt.theremin.feature.license.ui.model.LicenseEffect
import com.tkhskt.theremin.feature.license.ui.model.LicenseState
import com.tkhskt.theremin.feature.license.ui.model.LicenseUiState
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
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<LicenseEffect>()
    val sideEffect: SharedFlow<LicenseEffect> = _sideEffect

    private val _state = MutableStateFlow(LicenseState.INITIAL)
    val uiState: StateFlow<LicenseUiState> = _state
        .map { it.uiState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LicenseUiState.Initial,
        )

    fun dispatch(action: LicenseAction) {
        viewModelScope.launch {

            when (action) {
                is LicenseAction.Initialize -> {
                    _state.value = LicenseState(
                        artifacts = getArtifactsUseCase.invoke(),
                    )
                }

                is LicenseAction.ClickLicenseItem -> {
                    _sideEffect.emit(LicenseEffect.TransitToWebView(action.url))
                }

                is LicenseAction.ClickArtifactItem -> {
                    _sideEffect.emit(LicenseEffect.TransitToWebView(action.url))
                }
            }
        }
    }
}
