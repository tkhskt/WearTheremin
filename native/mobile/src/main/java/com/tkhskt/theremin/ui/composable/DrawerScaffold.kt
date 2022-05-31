package com.tkhskt.theremin.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun DrawerScaffold(
    backgroundColor: Color,
    mainContentGradientColors: List<Color>,
    titleContent: @Composable () -> Unit,
    drawerContent: @Composable () -> Unit,
    mainContent: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = LocalDensity.current.run { configuration.screenWidthDp.dp.toPx() }
    var drawerContentWidth by remember { mutableStateOf(0) }
    val maxOffset = max(screenWidth * 0.4f, drawerContentWidth * 0.9f)
    var progress by remember { mutableStateOf(0f) }

    Surface(color = backgroundColor) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                TitleContainer(
                    modifier = Modifier
                        .padding(18.dp)
                ) {
                    titleContent()
                }
                DrawerContentContainer(
                    modifier = Modifier.padding(top = 64.dp),
                    progress = progress,
                    onChangeContentWidth = { drawerContentWidth = it },
                ) {
                    drawerContent()
                }
            }
            MainContentContainer(
                contentGradientColors = mainContentGradientColors,
                modifier = Modifier.fillMaxSize(),
                screenWidth = screenWidth,
                maxOffset = maxOffset,
                onUpdateDrawerState = { progress = it },
            ) {
                mainContent()
            }
        }
    }
}

@Composable
private fun TitleContainer(
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        titleContent()
    }
}

@Composable
private fun DrawerContentContainer(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    onChangeContentWidth: (Int) -> Unit = {},
    drawerContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .onGloballyPositioned { coordinates ->
                onChangeContentWidth(coordinates.size.width)
            }
            .alpha(progress),
    ) {
        drawerContent()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainContentContainer(
    contentGradientColors: List<Color>,
    modifier: Modifier = Modifier,
    screenWidth: Float = 0f,
    maxOffset: Float = 0f,
    onUpdateDrawerState: (Float) -> Unit = { _ -> },
    mainContent: @Composable () -> Unit,
) {
    val swipeableState = rememberSwipeableState(0)
    // 0 to 1
    val progress = (swipeableState.offset.value / screenWidth) * 2.5f
    onUpdateDrawerState(progress)

    // 0 to 0.8
    val scale = 1f - (swipeableState.offset.value / screenWidth) * progress / 2
    // 0 to 10
    val corner = (swipeableState.offset.value / screenWidth) * progress * 25f
    // 0 to 8
    val elevation = (swipeableState.offset.value / screenWidth) * 7.5f

    Surface(
        modifier = modifier
            .offset {
                IntOffset(
                    x = swipeableState.offset.value.roundToInt(),
                    y = 0,
                )
            }
            .scale(scale)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(0f to 0, maxOffset to 1),
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal,
                resistance = null,
            ),
        shadowElevation = elevation.dp,
        shape = RoundedCornerShape((1.6 * corner).dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(contentGradientColors)
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            mainContent()
        }
        if (progress > 0) {
            Mask()
        }
    }
}

@Composable
private fun Mask() {
    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {}
    )
}
