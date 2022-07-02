package com.tkhskt.theremin.feature.license.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String) {
    val state = rememberWebViewState(url)
    WebView(
        state = state,
        onCreated = { it.settings.javaScriptEnabled = true },
    )
}
