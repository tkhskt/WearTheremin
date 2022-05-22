package com.tkhskt.theremin.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.ui.theme.LocalColorPalette

@Composable
fun Border(
    color: Color = LocalColorPalette.current.border,
) {
    Box(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(color),
    )
}
