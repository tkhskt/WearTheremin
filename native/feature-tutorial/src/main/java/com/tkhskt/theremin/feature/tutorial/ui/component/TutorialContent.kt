package com.tkhskt.theremin.feature.tutorial.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminTheme

@Composable
fun TutorialContent(
    title: String,
    body: String,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column {
                Text(
                    text = title,
                    style = ThereminTheme.typography.headlineMedium,
                )
                Text(
                    modifier = Modifier.padding(vertical = 32.dp),
                    text = body,
                    style = ThereminTheme.typography.bodyMedium,
                )
            }
        }
    }
}
