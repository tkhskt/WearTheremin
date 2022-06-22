package com.tkhskt.theremin.feature.theremin.ui.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tkhskt.theremin.core.ui.ThereminTheme

@Composable
fun NoteText(
    note: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = note,
        style = ThereminTheme.typography.displayLarge,
    )
}
