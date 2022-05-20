package com.tkhskt.theremin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightDefaultColorScheme = lightColorScheme(
    primary = Orange10,
    onPrimary = Color.White,
    secondary = Green10,
    onSecondary = Color.White,
    surface = Gray10,
    onSurface = Color.White,
    outline = Gray20,
)

@Composable
fun ThereminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightDefaultColorScheme,
        typography = ThereminTypography,
        content = content
    )
}
