package com.tkhskt.theremin.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.ui.theme.LocalColorPalette
import java.lang.Math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun Wave(
    frequency: Float,
    modifier: Modifier = Modifier,
) {

    val path = Path()

    Canvas(modifier = modifier
        .height(100.dp)
        .fillMaxWidth()
        .padding(vertical = 2.dp)
    ) {

        val height = size.height
        val width = size.width

        val amplitude = height / 2f

        val xPoints = (size.width * 5).roundToInt()

        (0..xPoints).forEach {
            val x = it / 5f

            val y = sin(x * (2f * PI * frequency / width)) * amplitude + (height / 2f)

            if (x == 0f) {
                path.moveTo(0f, height / 2f)
            } else {
                path.lineTo(x, y.toFloat())
            }
        }
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(
                width = 2.dp.toPx(),
                join = StrokeJoin.Round,
            ),
        )
    }
}

@Preview
@Composable
fun PreviewWave() {
    Surface(color = LocalColorPalette.current.highVolume1) {
        Wave(5f)
    }
}
