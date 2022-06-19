package com.tkhskt.theremin.feature.tutorial.ui

import com.tkhskt.theremin.core.ui.ThereminColorPalette
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialState
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialUiState

object TutorialUiStateMapper {
    fun mapFromState(state: TutorialState): TutorialUiState {
        return when (state.currentStep) {
            TutorialState.Step.PREPARATION -> {
                TutorialUiState(
                    title = "Preparation",
                    body = "Install and launch the WearTheremin app on your smartwatch.",
                    backgroundGradientColors = listOf(
                        ThereminColorPalette.midVolumeLight,
                        ThereminColorPalette.midVolume,
                    ),
                    buttonText = "Next",
                )
            }
            TutorialState.Step.VOLUME -> {
                TutorialUiState(
                    title = "Volume",
                    body = "The volume increases as you move your hand away from the front camera.\n" +
                            "\n" +
                            "The volume decreases as you move your hand closer to the front camera.",
                    backgroundGradientColors = listOf(
                        ThereminColorPalette.lowVolume2Light,
                        ThereminColorPalette.lowVolume2,
                    ),
                    buttonText = "Next",
                )
            }
            TutorialState.Step.PITCH -> {
                TutorialUiState(
                    title = "Pitch",
                    body = "The pitch changes when you tilt the smartwatch while wearing it.",
                    backgroundGradientColors = listOf(
                        ThereminColorPalette.lowVolume1Light,
                        ThereminColorPalette.lowVolume1,
                    ),
                    buttonText = "Start",
                )
            }
        }
    }
}
