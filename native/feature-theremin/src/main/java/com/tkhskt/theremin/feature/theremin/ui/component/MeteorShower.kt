package com.tkhskt.theremin.feature.theremin.ui.component

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun MeteorShower(
    modifier: Modifier = Modifier,
    onCompleteAnimation: () -> Unit = {},
) {
    val density = LocalDensity.current
    val shootingStarSize = 500.dp + 45.dp + 200.dp
    var initialOffset by remember { mutableStateOf(0f) }
    var targetOffset by remember {
        mutableStateOf(
            0f,
        )
    }
    var animatedOffset by remember { mutableStateOf(900f) }

    animatedOffset = animateFloatAsState(
        targetValue = targetOffset,
        animationSpec = tween(1000, easing = EaseIn),
        label = "",
    ) {
        onCompleteAnimation()
    }.value

    val configuration = LocalConfiguration.current
    val layoutHeight = configuration.screenHeightDp * 0.1
    Layout(
        modifier = modifier
            .fillMaxWidth()
            .height(layoutHeight.dp)
            .onSizeChanged {
                initialOffset = it.width.toFloat()
                targetOffset = it.width.toFloat() + with(density) {
                    shootingStarSize.toPx()
                }
            }
            .rotate(-30f),
        content = {
            (0..2).forEach {
                ShootingStar(
                    modifier = Modifier,
                    tailLength = 500.dp,
                    size = 45.dp,
                )
            }
        },
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            placeables.forEachIndexed { i, placeable ->
                placeable.placeRelative(x = initialOffset.roundToInt() - animatedOffset.roundToInt() + i * 150, y = (i * 150))
            }
        }
    }
}
