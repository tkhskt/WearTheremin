package com.tkhskt.theremin.feature.theremin.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun Sun(
    animate: Boolean,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val sunSize = configuration.screenWidthDp * 1.41f

    Layout(
        modifier = modifier
            .fillMaxWidth()
            .height((sunSize * 0.3).dp),
        content = {
            Circle(size = sunSize.dp)
            Circle(size = sunSize.dp, animate = animate, delay = 0)
            Circle(size = sunSize.dp, animate = animate, delay = 500)
            Circle(size = sunSize.dp, animate = animate, delay = 1000)
        },
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            val height = constraints.maxHeight
            val y = -(sunSize / 2f).dp.toPx().roundToInt() + height / 2
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = y)
            }
        }
    }
}

@Composable
private fun Circle(
    size: Dp,
    modifier: Modifier = Modifier,
    animate: Boolean = false,
    delay: Long = 0,
) {
    var start by remember { mutableStateOf(false) }

    val scale by createAnimationState(AnimationType.SCALE, start)
    val alpha by createAnimationState(AnimationType.ALPHA, start)

    LaunchedEffect(animate) {
        delay(delay)
        start = animate
    }

    Box(
        modifier = modifier
            .scale(scale)
            .alpha(alpha)
            .requiredSize(size)
            .clip(CircleShape)
            .background(ThereminTheme.color.sun),
    )
}

@Composable
private fun createAnimationState(
    type: AnimationType,
    start: Boolean = false,
    duration: Int = 2000,
): State<Float> {
    val targetValue = if (type == AnimationType.SCALE) {
        if (start) 1.7f else 1f
    } else {
        if (start) 0f else 1f
    }
    val tweenSpec: TweenSpec<Float> = tween(
        durationMillis = duration,
        easing = LinearEasing,
    )
    return animateFloatAsState(
        targetValue = targetValue,
        animationSpec = if (start) {
            infiniteRepeatable(
                animation = tweenSpec,
                repeatMode = RepeatMode.Restart,
            )
        } else {
            tweenSpec
        },
        label = ""
    )
}

private enum class AnimationType {
    SCALE,
    ALPHA,
}

@Preview
@Composable
fun PreviewSun() {
    Sun(false)
}
