package com.tkhskt.theremin.feature.license.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tkhskt.theremin.core.ui.ThereminTheme
import com.tkhskt.theremin.feature.license.ui.component.ArtifactItem
import com.tkhskt.theremin.feature.license.ui.component.LicenseItem
import com.tkhskt.theremin.feature.license.ui.model.LicenseAction
import com.tkhskt.theremin.feature.license.ui.model.LicenseUiState

@Composable
fun LicenseScreen(
    viewModel: LicenseViewModel = hiltViewModel(),
) {
    val state: LicenseUiState by viewModel.uiState.collectAsState()
    LicenseScreen(state, viewModel::dispatch)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LicenseScreen(
    uiState: LicenseUiState,
    dispatcher: (LicenseAction) -> Unit,
) {
    LaunchedEffect(Unit) {
        dispatcher(LicenseAction.Initialize)
    }
    Surface(
        color = Color.White,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                ),
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 48.dp, bottom = 16.dp),
                    text = "Open Source Licenses",
                    style = ThereminTheme.typography.headlineLarge,
                    color = ThereminTheme.color.licenseTitle,
                )
            }
            uiState.artifactGroups.forEachIndexed { index, artifactGroup ->
                stickyHeader {
                    LicenseItem(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(top = 36.dp, bottom = 8.dp),
                        name = artifactGroup.license.name,
                        onClick = { dispatcher(LicenseAction.ClickLicenseItem(url = artifactGroup.license.url)) },
                    )
                }
                items(artifactGroup.artifacts) { artifact ->
                    ArtifactItem(
                        name = artifact.name,
                        version = artifact.version,
                        onClick = if (artifact.url != null) {
                            { dispatcher(LicenseAction.ClickArtifactItem(url = artifact.url)) }
                        } else {
                            null
                        },
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.size(32.dp))
            }
        }
    }
}
