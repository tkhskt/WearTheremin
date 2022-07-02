package com.tkhskt.theremin.feature.license

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tkhskt.theremin.feature.license.ui.WebViewScreen

object WebViewGraph {
    const val route = "webView"
    const val webViewArg = "url"
}

fun NavGraphBuilder.webViewRoute() {
    composable(
        route = "${WebViewGraph.route}/{${WebViewGraph.webViewArg}}",
        arguments = listOf(navArgument(WebViewGraph.webViewArg) { type = NavType.StringType }),
    ) { backstackEntry ->
        backstackEntry.arguments?.getString(WebViewGraph.webViewArg)?.let { url ->
            WebViewScreen(url)
        }
    }
}
