package com.tkhskt.theremin.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tkhskt.theremin.R
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainUiState
import com.tkhskt.theremin.ui.theme.LocalColorPalette

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsState()
    MainScreen(state.value, viewModel::dispatch)
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    dispatcher: (MainAction) -> Unit,
) {
    DrawerScaffold(
        backgroundColor = LocalColorPalette.current.menuBackground,
        titleContent = {
            Image(
                modifier = Modifier.height(16.dp),
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
            )
        },
        drawerContent = {
            Menu(
                modifier = Modifier.padding(start = 20.dp),
                pcConnected = uiState.pcConnected,
                watchConnected = uiState.watchConnected,
                appSoundEnabled = uiState.appSoundEnabled,
                browserSoundEnabled = uiState.browserSoundEnabled,
                onClickAppButton = {
                    dispatcher(MainAction.ClickAppSoundButton)
                },
                onClickBrowserButton = {
                    dispatcher(MainAction.ClickBrowserSoundButton)
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            Sun()

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Wave(frequency = uiState.waveGraphicFrequency)

                NoteText(note = uiState.note)

                Button(
                    onClick = { dispatcher(MainAction.ClickStartWearableButton) }
                ) {
                    Text(text = "Start Wearable")
                }
                Button(
                    onClick = { dispatcher(MainAction.ClickCameraButton) }
                ) {
                    Text(text = "Start Camera")
                }
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    val uiState = MainUiState.Initial.copy(
        waveGraphicFrequency = 10f,
        note = "C#"
    )
    MainScreen(uiState) {}
}
