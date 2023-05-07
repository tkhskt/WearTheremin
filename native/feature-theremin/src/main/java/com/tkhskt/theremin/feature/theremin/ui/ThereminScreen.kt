package com.tkhskt.theremin.feature.theremin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.R
import com.tkhskt.theremin.core.ui.ThereminTheme
import com.tkhskt.theremin.core.ui.composable.MenuIcon
import com.tkhskt.theremin.core.ui.composable.ThereminScaffold
import com.tkhskt.theremin.core.ui.composable.rememberThereminScaffoldState
import com.tkhskt.theremin.core.ui.debounce
import com.tkhskt.theremin.feature.theremin.ui.component.Menu
import com.tkhskt.theremin.feature.theremin.ui.component.MeteorShower
import com.tkhskt.theremin.feature.theremin.ui.component.NoteText
import com.tkhskt.theremin.feature.theremin.ui.component.StarryBackground
import com.tkhskt.theremin.feature.theremin.ui.component.Sun
import com.tkhskt.theremin.feature.theremin.ui.component.Wave
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminUiState
import kotlinx.coroutines.launch

@Composable
fun ThereminScreen(
    uiState: ThereminUiState,
    onClickAppButton: (Boolean) -> Unit,
    onClickLicense: () -> Unit,
) {
    val scaffoldState = rememberThereminScaffoldState()
    val scope = rememberCoroutineScope()
    var backgroundColor by remember {
        mutableStateOf(uiState.backgroundGradientColors)
    }
    debounce(uiState.backgroundGradientColors) {
        backgroundColor = uiState.backgroundGradientColors
    }
    var meteorAnimationRunning by remember {
        mutableStateOf(false)
    }

    ThereminScaffold(
        backgroundColor = ThereminTheme.color.menuBackground,
        mainContentGradientColors = backgroundColor,
        scaffoldState = scaffoldState,
        titleContent = {
            Image(
                modifier = Modifier.height(20.dp),
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
            )
        },
        drawerContent = {
            Menu(
                modifier = Modifier.padding(start = 20.dp),
                watchConnected = uiState.watchConnected,
                appSoundEnabled = uiState.appSoundEnabled,
                onClickAppButton = onClickAppButton,
                onClickLicenseSection = onClickLicense,
            )
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            StarryBackground(starCount = 7)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Box {
                    if (uiState.showMeteor || meteorAnimationRunning) {
                        MeteorShower(modifier = Modifier.padding(top = 150.dp)) {
                            meteorAnimationRunning = false
                        }
                        meteorAnimationRunning = true
                    }
                    Sun(
                        animate = uiState.appSoundEnabled,
                    )
                    MenuIcon(
                        color = backgroundColor[1],
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                        modifier = Modifier.padding(20.dp),
                    )
                }
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Wave(frequency = uiState.waveGraphicFrequency)
                    Spacer(modifier = Modifier.size(26.5.dp))
                    NoteText(
                        modifier = Modifier.padding(start = 22.dp),
                        note = uiState.note,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    val uiState = ThereminUiState.Initial.copy(
        waveGraphicFrequency = 10f,
        note = "C#",
    )
    ThereminScreen(
        uiState = uiState,
        onClickLicense = {},
        onClickAppButton = {},
    )
}
