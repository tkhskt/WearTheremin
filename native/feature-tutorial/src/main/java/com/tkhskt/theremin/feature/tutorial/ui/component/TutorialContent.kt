package com.tkhskt.theremin.feature.tutorial.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminTheme

@Composable
fun TutorialContent(
    title: String,
    body: String,
    visible: Boolean,
    modifier: Modifier = Modifier,
    initialOffsetY: Float = 50f,
) {
    val density = LocalDensity.current
    Box(
        modifier = modifier,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 400),
            ) {
                with(density) { initialOffsetY.dp.roundToPx() }
            } + fadeIn(),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 200),
            ) {
                with(density) { initialOffsetY.dp.roundToPx() }
            } + fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    style = ThereminTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.size(36.dp))
                Text(
                    text = body,
                    style = ThereminTheme.typography.bodySmall,
                )
            }
        }
    }
}
