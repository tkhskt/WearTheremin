package com.tkhskt.theremin.feature.theremin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Star(modifier: Modifier = Modifier) {
    val rhomboidShape = GenericShape { size, _ ->
        moveTo(size.width / 2f, 0f)
        lineTo(size.width, size.height / 2f)
        lineTo(size.width / 2f, size.height)
        lineTo(0f, size.height / 2f)
        close()
    }

    Box(
        modifier = modifier
            .width(10.dp)
            .height(50.dp)
            .clip(rhomboidShape)
            .background(Color.White)
    )
}

@Preview
@Composable
fun PreviewStar() {
    Star()
}
