package com.tkhskt.theremin.ui.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun Sun(
    modifier: Modifier = Modifier,
    offset: Float,
) {
    val configuration = LocalConfiguration.current
    val sunSize = configuration.screenWidthDp * 1.41f
    Box(
        modifier = modifier
            .offset(y = (sunSize * offset).dp),
    ) {
        Circle(size = sunSize.dp)
        Circle(size = sunSize.dp, animate = true, delay = 0)
        Circle(size = sunSize.dp, animate = true, delay = 500)
        Circle(size = sunSize.dp, animate = true, delay = 1000)
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

    LaunchedEffect(Unit) {
        delay(delay)
        start = animate
    }

    Box(modifier = modifier
        .scale(scale)
        .alpha(alpha)
        .requiredSize(size)
        .clip(CircleShape)
        .background(Color.White)
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
    return animateFloatAsState(
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        )
    )
}

private enum class AnimationType {
    SCALE,
    ALPHA,
}

@Preview
@Composable
fun PreviewSun() {
    Sun(offset = 0f)
}
