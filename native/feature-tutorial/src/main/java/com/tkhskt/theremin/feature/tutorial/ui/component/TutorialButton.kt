package com.tkhskt.theremin.feature.tutorial.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminTheme

@Composable
fun TutorialButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 3.dp, start = 3.dp)
                .defaultMinSize(minWidth = 124.dp, minHeight = 32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ThereminTheme.color.buttonShadow),
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .defaultMinSize(minWidth = 124.dp, minHeight = 32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ThereminTheme.color.button)
                .clickable { onClick.invoke() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = textColor,
                style = ThereminTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
fun PreviewTutorialButton() {
    TutorialButton(
        text = "Next",
        onClick = {},
        textColor = ThereminTheme.color.midVolume,
    )
}
