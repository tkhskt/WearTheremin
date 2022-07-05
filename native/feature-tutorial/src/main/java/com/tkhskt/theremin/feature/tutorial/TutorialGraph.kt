package com.tkhskt.theremin.feature.tutorial

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tkhskt.theremin.feature.tutorial.ui.TutorialScreen

object TutorialDestination {
    const val route = "tutorial"
}

fun NavGraphBuilder.tutorialGraph(
    navigateToTheremin: () -> Unit,
) {
    composable(
        route = TutorialDestination.route,
    ) {
        TutorialScreen(
            navigateToTheremin = navigateToTheremin,
        )
    }
}
