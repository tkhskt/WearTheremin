package com.tkhskt.theremin.feature.tutorial.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.tkhskt.theremin.feature.tutorial.ui.component.TutorialButton
import com.tkhskt.theremin.feature.tutorial.ui.component.TutorialContent
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialAction
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialEffect
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialUiState
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(
    viewModel: TutorialViewModel = hiltViewModel(),
    navigateToTheremin: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState: TutorialUiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.sideEffect.collect {
                if (it is TutorialEffect.TransitToTheremin) {
                    navigateToTheremin()
                }
            }
        }
    }
    TutorialScreen(
        uiState = uiState,
        dispatcher = viewModel::dispatch,
    )
}

@Composable
fun TutorialScreen(
    uiState: TutorialUiState,
    dispatcher: (TutorialAction) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenWidthDp
    val contentTopPaddingDp = screenHeight * 0.313f

    var visible by remember(uiState.title) { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier,
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        uiState.backgroundGradientColors.map {
                            animateColorAsState(targetValue = it, label = "").value
                        },
                    ),
                )
                .fillMaxSize()
                .padding(
                    top = contentTopPaddingDp.dp,
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 48.dp,
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            TutorialContent(
                title = uiState.title,
                body = uiState.body,
                visible = visible,
            )

            TutorialButton(
                text = uiState.buttonText,
                onClick = {
                    coroutineScope.launch {
                        visible = false
                        dispatcher(TutorialAction.ClickStepButton)
                    }
                },
                textColor = uiState.backgroundGradientColors[1],
            )
        }
    }
}
