package com.tkhskt.theremin.core.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminColorPalette

@Composable
fun MenuIcon(
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            Modifier
                .width(28.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(color),
        )
        Box(
            Modifier
                .width(16.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(color),
        )
        Box(
            Modifier
                .width(22.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(color),
        )
    }
}

@Preview
@Composable
fun PreviewMenuIcon() {
    MenuIcon(
        color = ThereminColorPalette.lowVolume1,
        onClick = {},
    )
}
