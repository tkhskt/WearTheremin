package com.tkhskt.theremin.ui.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tkhskt.theremin.ui.theme.LocalTypography

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
