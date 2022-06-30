package com.tkhskt.theremin.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
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

object ThereminColorPalette {
    val text = Color.White
    val button = Color.White
    val star = Color.White
    val sun = Color.White
    val wave = Color.White
    val buttonShadow = Gray20
    val lowVolume1 = Blue10
    val lowVolume1Light = Blue20
    val lowVolume2 = Green10
    val lowVolume2Light = Green20
    val midVolume = Orange10
    val midVolumeLight = Orange20
    val highVolume1 = Pink10
    val highVolume1Light = Pink20
    val highVolume2 = Red10
    val highVolume2Light = Red20
    val menuBackground = Gray10
    val border = Gray30
    val buttonEnabled = Green10
    val buttonDisabled = Gray40
    val licenseName = Gray50
    val licenseTitle = Black10
    val artifactName = Black10
    val versionText = Gray50
    val licenseDivider = Gray60
}

internal val LocalColorPalette = staticCompositionLocalOf { ThereminColorPalette }

internal val LocalTypography = staticCompositionLocalOf { ThereminTypography }

object ThereminTheme {
    val color
        @Composable
        get() = LocalColorPalette.current

    val typography
        @Composable
        get() = LocalTypography.current
}

@Composable
fun ThereminTheme(content: @Composable () -> Unit) {
    val typography = ThereminTypography
    val thereminColors = ThereminColorPalette
    CompositionLocalProvider(
        LocalTypography provides typography,
        LocalColorPalette provides thereminColors,
    ) {
        MaterialTheme(
            colorScheme = LightDefaultColorScheme,
            typography = ThereminTypography,
            content = content,
        )
    }
}
