package com.tkhskt.theremin.mobile

import android.app.Activity
import android.content.Context
import android.hardware.SensorManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.tkhskt.theremin.core.ui.ThereminColorPalette
import com.tkhskt.theremin.core.ui.ThereminTheme
import com.tkhskt.theremin.feature.license.LicenseDestination
import com.tkhskt.theremin.feature.license.WebViewDestination
import com.tkhskt.theremin.feature.license.licenseGraph
import com.tkhskt.theremin.feature.license.webViewRoute
import com.tkhskt.theremin.feature.theremin.ThereminDestination
import com.tkhskt.theremin.feature.theremin.thereminGraph
import com.tkhskt.theremin.feature.theremin.ui.HandTracker
import com.tkhskt.theremin.feature.theremin.ui.OscillatorController
import com.tkhskt.theremin.feature.theremin.ui.ShakeDetector
import com.tkhskt.theremin.feature.theremin.ui.ThereminViewModel
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction
import com.tkhskt.theremin.feature.tutorial.TutorialDestination
import com.tkhskt.theremin.feature.tutorial.tutorialGraph

@Composable
fun WearTheremin(
    viewModel: ThereminViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current as Activity
    val oscillatorController = remember { OscillatorController() }
    val jankDetector = remember { JankDetector() }
    val handTracker = remember { HandTracker(activity) }
    val shakeDetector = remember {
        ShakeDetector(activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager) {
            viewModel.dispatch(ThereminAction.Shake)
        }
    }
    val navController = rememberNavController()

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = ThereminColorPalette.menuBackground,
            darkIcons = useDarkIcons,
        )
        onDispose {}
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.apply {
            addObserver(oscillatorController)
            addObserver(jankDetector)
            addObserver(shakeDetector)
        }
        jankDetector.startDetection(
            stateName = "ThereminApp",
            window = activity.window,
        )
        onDispose {
            lifecycleOwner.lifecycle.apply {
                removeObserver(handTracker)
                removeObserver(oscillatorController)
                removeObserver(jankDetector)
                removeObserver(shakeDetector)
            }
        }
    }

    ThereminTheme {
        NavHost(
            navController = navController,
            startDestination = TutorialDestination.route,
        ) {
            tutorialGraph(
                navigateToTheremin = {
                    navController.navigate(ThereminDestination.route) {
                        popUpTo(TutorialDestination.route) {
                            inclusive = true
                        }
                    }
                },
            )
            licenseGraph(
                navigateToWebView = { navController.navigate("${WebViewDestination.route}/$it") },
                nestedGraphs = { webViewRoute() },
            )
            thereminGraph(
                viewModel = viewModel,
                oscillatorController = oscillatorController,
                handTracker = handTracker,
                navigateToLicense = {
                    navController.navigate(LicenseDestination.route)
                },
            )
        }
    }
}
