package com.tkhskt.theremin.feature.license

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tkhskt.theremin.feature.license.ui.WebViewScreen

object WebViewDestination {
    const val route = "webView"
    const val webViewArg = "url"
}

fun NavGraphBuilder.webViewRoute() {
    composable(
        route = "${WebViewDestination.route}/{${WebViewDestination.webViewArg}}",
        arguments = listOf(navArgument(WebViewDestination.webViewArg) { type = NavType.StringType }),
    ) { backstackEntry ->
        backstackEntry.arguments?.getString(WebViewDestination.webViewArg)?.let { url ->
            WebViewScreen(url)
        }
    }
}
