package com.tkhskt.theremin

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tkhskt.theremin.ui.HandTracker
import com.tkhskt.theremin.ui.JankDetector
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.OscillatorController
import com.tkhskt.theremin.ui.composable.MainScreen
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import com.tkhskt.theremin.ui.theme.ThereminTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val handTracker by lazy {
        HandTracker(this) { distance: Float ->
            viewModel.dispatch(MainAction.ChangeDistance(distance))
        }
    }

    private val oscillatorController = OscillatorController()

    private val jankDetector = JankDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        supportActionBar?.hide()
        lifecycle.apply {
            addObserver(handTracker)
            addObserver(oscillatorController)
            addObserver(jankDetector)
        }
        collectEffect()
        initBluetooth()
        jankDetector.startDetection(
            stateName = MainActivity::class.java.simpleName,
            window = window,
        )
        setContent {
            ThereminTheme {
                MainScreen()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                viewModel.dispatch(MainAction.InitializeBle)
            } else {
                Toast.makeText(this, "Please grant permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun collectEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.sideEffect.collect { effect ->
                    when (effect) {
                        is MainEffect.StartCamera -> {
                            handTracker.startTracking()
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    oscillatorController.run {
                        changeFrequency(it.frequency)
                        changeVolume(it.volume)
                        playSound(it.appSoundEnabled)
                    }
                }
            }
        }
    }

    private fun initBluetooth() {
        if (bluetoothPermissionGranted) {
            viewModel.dispatch(MainAction.InitializeBle)
        } else {
            requestBluetoothPermission(PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
    }
}
