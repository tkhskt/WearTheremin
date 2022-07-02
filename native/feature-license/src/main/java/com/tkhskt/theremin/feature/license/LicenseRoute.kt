package com.tkhskt.theremin.feature.license

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.tkhskt.theremin.feature.license.ui.LicenseScreen

object LicenseDestination {
    const val route = "license_route"
    const val destination = "license"
}

fun NavGraphBuilder.licenseRoute(
    navigateToWebView: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = LicenseDestination.route,
        startDestination = LicenseDestination.destination,
    ) {
        composable(
            route = LicenseDestination.destination,
        ) {
            LicenseScreen(
                navigateToWebView = navigateToWebView,
            )
        }
        nestedGraphs()
    }
}
