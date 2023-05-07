package com.tkhskt.theremin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun debounce(
    key: Any,
    delayMs: Long = 150,
    execution: () -> Unit,
) {
    LaunchedEffect(key) {
        delay(delayMs)
        execution()
    }
}
