package com.tkhskt.theremin.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.ui.theme.LocalColorPalette
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToggleButton(
    checked: Boolean = false,
    onChangeButtonStatus: (Boolean) -> Unit,
) {
    val circleSize = 22.dp
    val toggleButtonWidth = 48.dp

    val coroutineScope = rememberCoroutineScope()
    val swipeableState = rememberSwipeableState(initialValue = checked)
    val minOffset = with(LocalDensity.current) { 2.dp.toPx() }
    val maxOffset = with(LocalDensity.current) { (toggleButtonWidth - circleSize - 2.dp).toPx() }
    val colorAnimation = animateColorAsState(
        targetValue = if (swipeableState.targetValue) {
            LocalColorPalette.current.buttonEnabled
        } else {
            LocalColorPalette.current.buttonDisabled
        }
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .width(48.dp)
            .height(26.dp)
            .background(color = colorAnimation.value)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(minOffset to false, maxOffset to true),
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal,
                resistance = null,
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .clip(CircleShape)
                .size(circleSize)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    coroutineScope.launch {
                        onChangeButtonStatus(!swipeableState.currentValue)
                        swipeableState.animateTo(!swipeableState.currentValue)
                    }
                }
                .background(Color.White),
        )
    }
}

@Preview(name = "not checked")
@Composable
fun PreviewToggleButtonNotChecked() {
    ToggleButton(checked = false, onChangeButtonStatus = {})
}

@Preview(name = "checked")
@Composable
fun PreviewToggleButtonChecked() {
    ToggleButton(checked = true, onChangeButtonStatus = {})
}
