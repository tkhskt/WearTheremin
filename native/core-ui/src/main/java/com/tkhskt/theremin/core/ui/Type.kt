package com.tkhskt.theremin.core.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ThereminTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 116.sp,
        lineHeight = 153.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = Color.White,
    ),
)

val LocalTypography = staticCompositionLocalOf { ThereminTypography }
