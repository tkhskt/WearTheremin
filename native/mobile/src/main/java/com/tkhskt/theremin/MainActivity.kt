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
import com.tkhskt.theremin.ui.HandDetector
import com.tkhskt.theremin.ui.JankStatsManager
import com.tkhskt.theremin.ui.MainScreen
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.OscillatorController
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val handDetector by lazy {
        HandDetector(this) { distance: Float ->
            viewModel.dispatch(MainAction.ChangeDistance(distance))
        }
    }

    private val oscillatorController = OscillatorController()

    private val jankStatsManager = JankStatsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        lifecycle.apply {
            addObserver(handDetector)
            addObserver(oscillatorController)
            addObserver(jankStatsManager)
        }
        collectEffect()
        initBluetooth()
        jankStatsManager.startMeasure(
            stateName = MainActivity::class.java.simpleName,
            window = window,
        )
        setContent {
            MainScreen()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
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
                            handDetector.startHandDetection()
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
