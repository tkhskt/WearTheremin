package com.tkhskt.theremin.feature.license.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tkhskt.theremin.core.ui.ThereminTheme

@Composable
fun LicenseItem(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        text = name,
        style = ThereminTheme.typography.bodyMedium,
        color = ThereminTheme.color.licenseName,
    )
}
