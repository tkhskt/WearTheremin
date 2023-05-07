package com.tkhskt.theremin.feature.theremin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tkhskt.theremin.feature.theremin.ui.HandTracker
import com.tkhskt.theremin.feature.theremin.ui.OscillatorController
import com.tkhskt.theremin.feature.theremin.ui.ThereminRoute
import com.tkhskt.theremin.feature.theremin.ui.ThereminViewModel

object ThereminDestination {
    const val route = "theremin"
}

fun NavGraphBuilder.thereminGraph(
    viewModel: ThereminViewModel,
    oscillatorController: OscillatorController,
    handTracker: HandTracker,
    navigateToLicense: () -> Unit,
) {
    composable(
        route = ThereminDestination.route,
    ) {
        ThereminRoute(
            viewModel = viewModel,
            oscillatorController = oscillatorController,
            handTracker = handTracker,
            navigateToLicense = navigateToLicense,
        )
    }
}
