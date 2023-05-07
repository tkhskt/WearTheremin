package com.tkhskt.theremin.feature.theremin.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminColorPalette
import com.tkhskt.theremin.core.ui.ThereminTheme


@Composable
fun Tail(
    width: Dp,
    length: Dp,
    modifier: Modifier = Modifier,
) {
    val tailShape = GenericShape { size, _ ->
        moveTo(0f, 0f)
        lineTo(size.width, size.height * 0.15f)
        lineTo(size.width, size.height * 0.85f)
        lineTo(0f, size.height)
        close()
    }
    Box(
        modifier = modifier
            .width(length)
            .height(width)
            .clip(tailShape)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.8f),
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.1f),
                    ),
                ),
            ),
    )
}

@Composable
fun ShootingStar(
    modifier: Modifier = Modifier,
    tailLength: Dp,
    size: Dp,
) {
    // Allow resume on rotation
    var currentRotation by remember { mutableStateOf(0f) }

    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(Unit) {
        // Infinite repeatable rotation when is playing
        rotation.animateTo(
            targetValue = currentRotation + 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        ) {
            currentRotation = value
        }
    }

    val starShape = GenericShape { shapeSize, _ ->
        moveTo(shapeSize.width / 2f, 0f)
        cubicTo(
            shapeSize.width / 2f,
            0f,
            (shapeSize.width * 1f / 2f) + shapeSize.width / 10f,
            (shapeSize.height * 1f / 2f) - shapeSize.height / 10f,
            shapeSize.width,
            shapeSize.height / 2f,
        )
        cubicTo(
            shapeSize.width,
            shapeSize.height / 2f,
            (shapeSize.width * 1f / 2f) + shapeSize.width / 10f,
            (shapeSize.height * 1f / 2f) + shapeSize.height / 10f,
            shapeSize.width / 2f,
            shapeSize.height,
        )
        cubicTo(
            shapeSize.width / 2f,
            shapeSize.height,
            (shapeSize.width * 1f / 2f) - shapeSize.width / 10f,
            (shapeSize.height * 1f / 2f) + shapeSize.height / 10f,
            0f,
            shapeSize.height / 2f,
        )
        cubicTo(
            0f,
            shapeSize.height / 2f,
            (shapeSize.width * 1f / 2f) - shapeSize.width / 10f,
            (shapeSize.height * 1f / 2f) - shapeSize.height / 10f,
            shapeSize.width / 2f,
            0f,
        )
        close()
    }

    Box(modifier = modifier.wrapContentHeight()) {
        Box(
            modifier = Modifier
                .rotate(rotation.value)
                .width(size)
                .height(size)
                .clip(starShape)
                .background(ThereminTheme.color.star)

        )
        Tail(
            modifier = Modifier
                .padding(start = size / 2f)
                .align(Alignment.CenterStart),
            width = 25.dp,
            length = tailLength,
        )
    }
}

@Preview
@Composable
fun PreviewShootingStar() {
    Surface(color = ThereminColorPalette.lowVolume1) {
        ShootingStar(
            size = 50.dp,
            tailLength = 300.dp,
        )
    }
}
