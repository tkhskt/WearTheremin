package com.tkhskt.theremin.feature.theremin.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminTheme
import java.lang.Math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun Wave(
    frequency: Float,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = LocalDensity.current.run { configuration.screenWidthDp.dp.toPx() }
    val waveHeightDp = screenHeight * 0.145f
    val waveColor = ThereminTheme.color.wave

    Canvas(
        modifier = modifier
            .height(waveHeightDp.dp)
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        val path = Path()

        val height = size.height
        val width = size.width

        val amplitude = height / 2f

        val xMagnification = 2f

        val xPointCount = (size.width * xMagnification).roundToInt()

        (0..xPointCount).forEach { x ->
            val y =
                sin(x * (2f * PI * frequency * xMagnification / xPointCount)) * amplitude + (height / 2f)
            if (x == 0) {
                path.moveTo(0f, height / 2f)
            } else {
                path.lineTo(x.toFloat(), y.toFloat())
            }
        }
        withTransform({
            translate(left = -(width / 2f))
        }) {
            drawPath(
                path = path,
                color = waveColor,
                style = Stroke(
                    width = 4.dp.toPx(),
                    join = StrokeJoin.Round,
                ),
            )
        }
    }
}

@Preview
@Composable
fun PreviewWave() {
    Surface(color = ThereminTheme.color.highVolume1) {
        Wave(5f)
    }
}
