package com.tkhskt.theremin.core.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
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
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
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

enum class ThereminDrawerValue {
    CLOSE,
    OPEN,
}

class ThereminScaffoldState(
    val drawerState: ThereminDrawerState,
)

@OptIn(ExperimentalMaterialApi::class)
class ThereminDrawerState {

    val swipeableState = SwipeableState(
        initialValue = ThereminDrawerValue.CLOSE,
        confirmStateChange = { true },
    )

    suspend fun open() = animateTo(ThereminDrawerValue.OPEN, SwipeableDefaults.AnimationSpec)

    @ExperimentalMaterialApi
    suspend fun animateTo(targetValue: ThereminDrawerValue, anim: AnimationSpec<Float>) {
        swipeableState.animateTo(targetValue, anim)
    }
}

@Composable
fun rememberThereminScaffoldState(
    drawerState: ThereminDrawerState = remember { ThereminDrawerState() },
): ThereminScaffoldState = remember { ThereminScaffoldState(drawerState) }


@Composable
fun ThereminScaffold(
    backgroundColor: Color,
    mainContentGradientColors: List<Color>,
    scaffoldState: ThereminScaffoldState = rememberThereminScaffoldState(),
    titleContent: @Composable () -> Unit = {},
    drawerContent: @Composable () -> Unit = {},
    mainContent: @Composable () -> Unit = {},
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
                        .padding(18.dp),
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
                drawerState = scaffoldState.drawerState,
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
    drawerState: ThereminDrawerState = remember { ThereminDrawerState() },
    onUpdateDrawerState: (Float) -> Unit = { _ -> },
    mainContent: @Composable () -> Unit = {},
) {
    // 0 to 1
    val progress = (drawerState.swipeableState.offset.value / screenWidth) * 2.5f
    onUpdateDrawerState(progress)

    // 0 to 0.8
    val scale = 1f - (drawerState.swipeableState.offset.value / screenWidth) * progress / 2
    // 0 to 10
    val corner = (drawerState.swipeableState.offset.value / screenWidth) * progress * 25f
    // 0 to 8
    val elevation = (drawerState.swipeableState.offset.value / screenWidth) * 7.5f

    Surface(
        modifier = modifier
            .offset {
                IntOffset(
                    x = drawerState.swipeableState.offset.value.roundToInt(),
                    y = 0,
                )
            }
            .scale(scale)
            .swipeable(
                state = drawerState.swipeableState,
                anchors = mapOf(0f to ThereminDrawerValue.CLOSE, maxOffset to ThereminDrawerValue.OPEN),
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
                    Brush.verticalGradient(
                        contentGradientColors.map {
                            animateColorAsState(targetValue = it).value
                        },
                    ),
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
            .pointerInput(Unit) {},
    )
}
