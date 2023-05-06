package com.tkhskt.theremin.feature.tutorial.ui.model

import androidx.compose.ui.graphics.Color
import com.tkhskt.theremin.core.ui.ThereminColorPalette

data class TutorialUiState(
    val backgroundGradientColors: List<Color>,
    val title: String,
    val body: String,
    val buttonText: String,
) {
    companion object {
        val Initial = TutorialUiState(
            backgroundGradientColors = listOf(
                ThereminColorPalette.midVolume,
                ThereminColorPalette.midVolumeLight,
            ),
            title = "",
            body = "",
            buttonText = "",
        )
    }
}
