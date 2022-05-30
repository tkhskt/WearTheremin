package com.tkhskt.theremin.ui.composable

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StarryBackground(
    @IntRange(from = 1, to = 6) starCount: Int,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            (0 until starCount).forEach { _ ->
                Star()
            }
        },
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        layout(
            width = width,
            height = height
        ) {
            placeables.forEachIndexed { index, placeable ->
                val position = Position.values()[index].toAbsolutePosition(width, height)
                placeable.place(position.first, position.second)
            }
        }
    }
}

private enum class Position(val x: Int, val y: Int) {
    STAR_1(16, 14),
    STAR_2(90, 18),
    STAR_3(57, 22),
    STAR_4(17, 37),
    STAR_5(81, 42),
    STAR_6(76, 83);

    fun toAbsolutePosition(baseWidth: Int, baseHeight: Int): Pair<Int, Int> {
        val absX = x * (baseWidth / 100)
        val absY = y * (baseHeight / 100)
        return absX to absY
    }
}

@Preview
@Composable
fun PreviewStarryBackground() {
    StarryBackground(starCount = 6)
}
