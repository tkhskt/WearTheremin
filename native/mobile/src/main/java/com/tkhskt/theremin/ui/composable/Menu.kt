package com.tkhskt.theremin.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.R
import com.tkhskt.theremin.ui.theme.LocalColorPalette
import com.tkhskt.theremin.ui.theme.LocalTypography

@Composable
fun Menu(
    modifier: Modifier = Modifier,
    pcConnected: Boolean = false,
    watchConnected: Boolean = false,
    appSoundEnabled: Boolean = false,
    browserSoundEnabled: Boolean = false,
    onClickAppButton: (Boolean) -> Unit = {},
    onClickBrowserButton: (Boolean) -> Unit = {},
    onClickLicenseSection: () -> Unit = {},
) {
    val sectionSpace = 28.dp
    Column(
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        DeviceSection(
            pcConnected = pcConnected,
            watchConnected = watchConnected,
        )
        Spacer(modifier = Modifier.size(sectionSpace))
        SoundSection(
            browserSoundEnabled = browserSoundEnabled,
            appSoundEnabled = appSoundEnabled,
            onClickAppButton = onClickAppButton,
            onClickBrowserButton = onClickBrowserButton,
        )
        Spacer(modifier = Modifier.size(sectionSpace))
        LicenseSection(onClick = onClickLicenseSection)
    }
}

@Composable
private fun DeviceSection(
    modifier: Modifier = Modifier,
    pcConnected: Boolean = false,
    watchConnected: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        MenuSectionTitle(title = "Device")
        SubSectionContainer {
            IconMenuItem(
                iconRes = R.drawable.ic_drawer_wear,
                text = if (watchConnected) "Connected" else "Disconnected"
            )
            IconMenuItem(
                iconRes = R.drawable.ic_drawer_pc,
                text = if (pcConnected) "Connected" else "Disconnected",
            )
        }
    }
}

@Composable
private fun SoundSection(
    modifier: Modifier = Modifier,
    appSoundEnabled: Boolean = false,
    browserSoundEnabled: Boolean = false,
    onClickAppButton: (Boolean) -> Unit = {},
    onClickBrowserButton: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        MenuSectionTitle(title = "Sound")
        SubSectionContainer {
            ToggleButtonMenuItem(
                text = "App",
                buttonEnabled = appSoundEnabled,
                onChangeButtonStatus = onClickAppButton,
            )
            ToggleButtonMenuItem(
                text = "Browser",
                buttonEnabled = browserSoundEnabled,
                onChangeButtonStatus = onClickBrowserButton,
            )
        }
    }
}

@Composable
private fun LicenseSection(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.clickable {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "License",
                style = LocalTypography.current.headlineSmall,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "Arrow",
            )
        }
        Spacer(modifier = Modifier.size(3.dp))
        Border()
    }
}

@Composable
private fun MenuSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = LocalTypography.current.headlineSmall,
        )
        Spacer(modifier = Modifier.size(3.dp))
        Border()
    }
}

@Composable
private fun SubSectionContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.padding(top = 4.dp, start = 14.dp)
    ) {
        content()
    }
}

@Composable
private fun IconMenuItem(
    @DrawableRes iconRes: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .defaultMinSize(
                minHeight = 28.dp
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 12.dp,
                bottom = 8.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(28.dp))
            Text(
                text = text,
                style = LocalTypography.current.bodySmall,
            )
        }
        Border()
    }
}

@Composable
private fun ToggleButtonMenuItem(
    text: String,
    modifier: Modifier = Modifier,
    buttonEnabled: Boolean = false,
    onChangeButtonStatus: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier
            .defaultMinSize(
                minHeight = 28.dp
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 12.dp,
                    bottom = 8.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = text,
                style = LocalTypography.current.bodySmall,
            )
            ToggleButton(
                checked = buttonEnabled,
                onChangeButtonStatus = onChangeButtonStatus,
            )
        }
        Border()
    }
}

@Preview
@Composable
fun PreviewMenu() {
    Surface(color = LocalColorPalette.current.menuBackground) {
        Menu()
    }
}
