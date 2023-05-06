package com.tkhskt.theremin.feature.tutorial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialAction
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialEffect
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialState
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class TutorialViewModel @Inject constructor() : ViewModel() {

    private val _sideEffect = MutableSharedFlow<TutorialEffect>()
    val sideEffect: SharedFlow<TutorialEffect> = _sideEffect.asSharedFlow()

    private val _state = MutableStateFlow(TutorialState.INITIAL)
    val uiState: StateFlow<TutorialUiState> = _state
        .map { it.uiState }
        .stateIn(
            scope = viewModelScope,
            initialValue = TutorialUiState.Initial,
            started = SharingStarted.Lazily,
        )

    fun dispatch(action: TutorialAction) {
        viewModelScope.launch {
            when (action) {
                is TutorialAction.ClickStepButton -> {
                    if (_state.value.currentStep == TutorialState.Step.PITCH) {
                        _sideEffect.emit(TutorialEffect.TransitToTheremin)
                    }
                    val nextStep = when (_state.value.currentStep) {
                        TutorialState.Step.PREPARATION -> {
                            delay(DELAY_FOR_ANIMATION)
                            TutorialState.Step.VOLUME
                        }

                        TutorialState.Step.VOLUME -> {
                            delay(DELAY_FOR_ANIMATION)
                            TutorialState.Step.PITCH
                        }

                        TutorialState.Step.PITCH -> TutorialState.Step.PITCH
                    }
                    _state.update { it.copy(currentStep = nextStep) }
                }
            }

        }
    }

    companion object {
        private const val DELAY_FOR_ANIMATION = 500L
    }
}
