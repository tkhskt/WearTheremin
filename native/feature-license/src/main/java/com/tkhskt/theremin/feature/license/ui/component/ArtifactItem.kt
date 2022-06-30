package com.tkhskt.theremin.feature.license.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tkhskt.theremin.core.ui.ThereminTheme
import com.tkhskt.theremin.feature.license.R

@Composable
fun ArtifactItem(
    name: String,
    version: String,
    clickable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp)
            .run {
                if (clickable) clickable { onClick() } else this
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(
                    text = name,
                    style = ThereminTheme.typography.bodyMedium,
                    color = ThereminTheme.color.artifactName,
                )
                Text(
                    text = version,
                    style = ThereminTheme.typography.bodySmall,
                    color = ThereminTheme.color.versionText,
                )
            }
            if (clickable) {
                Image(
                    modifier = Modifier.padding(start = 16.dp),
                    painter = painterResource(id = R.drawable.ic_license_arrow),
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.size(5.5.dp))
        Divider(
            color = ThereminTheme.color.licenseDivider,
        )
    }
}

@Preview
@Composable
fun PreviewArtifactItem() {
    ThereminTheme {
        ArtifactItem(
            name = "Compose Animation",
            version = "1.2.0-alpha04",
            clickable = true,
            onClick = {},
        )
    }
}
