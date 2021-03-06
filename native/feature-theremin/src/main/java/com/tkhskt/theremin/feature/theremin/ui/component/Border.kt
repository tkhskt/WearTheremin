package com.tkhskt.theremin.feature.theremin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminTheme

@Composable
fun Border(
    color: Color = ThereminTheme.color.border,
) {
    Box(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(color),
    )
}
