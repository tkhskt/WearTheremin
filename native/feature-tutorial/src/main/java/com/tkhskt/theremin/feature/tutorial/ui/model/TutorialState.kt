package com.tkhskt.theremin.feature.tutorial.ui.model

import com.tkhskt.theremin.core.ui.ThereminColorPalette

data class TutorialState(
    val currentStep: Step,
) {
    enum class Step {
        PREPARATION,
        VOLUME,
        PITCH,
    }

    companion object {
        val INITIAL = TutorialState(
            currentStep = Step.PREPARATION,
        )
    }

    val uiState = when (currentStep) {
        Step.PREPARATION -> {
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

        Step.VOLUME -> {
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

        Step.PITCH -> {
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
