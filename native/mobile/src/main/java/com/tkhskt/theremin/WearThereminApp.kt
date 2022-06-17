package com.tkhskt.theremin

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tkhskt.theremin.core.ui.ThereminTheme
import com.tkhskt.theremin.feature.theremin.PermissionRequestState
import com.tkhskt.theremin.feature.theremin.bluetoothPermissionGranted
import com.tkhskt.theremin.feature.theremin.requestBluetoothPermissions
import com.tkhskt.theremin.feature.theremin.ui.HandTracker
import com.tkhskt.theremin.feature.theremin.ui.ThereminViewModel
import com.tkhskt.theremin.feature.theremin.ui.OscillatorController
import com.tkhskt.theremin.feature.theremin.ui.ThereminScreen
import com.tkhskt.theremin.feature.theremin.ui.model.ThereminAction

@Composable
fun WearThereminApp(
    viewModel: ThereminViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current as Activity
    val oscillatorController = remember { OscillatorController() }
    val jankDetector = remember { JankDetector() }
    val handTracker = remember { HandTracker(activity) }
    val navController = rememberNavController()

    var bluetoothPermissionRequestState: PermissionRequestState by remember {
        mutableStateOf(PermissionRequestState.NotRequested)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            bluetoothPermissionRequestState =
                if (result.values.all { true }) {
                    PermissionRequestState.Granted
                } else {
                    PermissionRequestState.Denied
                }
        }

    val permissionHandler = remember {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                if (activity.bluetoothPermissionGranted) {
                    bluetoothPermissionRequestState = PermissionRequestState.Granted
                } else {
                    launcher.requestBluetoothPermissions()
                }
            }
        }
    }

    when (bluetoothPermissionRequestState) {
        PermissionRequestState.Granted -> {
            viewModel.dispatch(ThereminAction.InitializeBle)
            lifecycleOwner.lifecycle.addObserver(handTracker)
        }
        PermissionRequestState.Denied -> {
            Toast.makeText(
                activity,
                "Please grant permissions",
                Toast.LENGTH_SHORT
            ).show()
        }
        else -> {
            // no-op
        }
    }

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.apply {
            addObserver(oscillatorController)
            addObserver(jankDetector)
            addObserver(permissionHandler)
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
                removeObserver(permissionHandler)
            }
        }
    }

    ThereminTheme {
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable(
                route = "tutorial"
            ) {
            }
            composable(
                route = "main"
            ) {
                ThereminScreen(
                    viewModel = viewModel,
                    oscillatorController = oscillatorController,
                    handTracker = handTracker,
                )
            }
        }
    }
}
