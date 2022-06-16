package com.tkhskt.theremin.feature.theremin.ui.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tkhskt.theremin.core.ui.LocalTypography

@Composable
fun NoteText(
    note: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = note,
        style = LocalTypography.current.displayLarge,
    )
}