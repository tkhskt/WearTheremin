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
import com.tkhskt.theremin.ui.MainScreen
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.OscillatorManager
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val handDetector by lazy {
        HandDetector(this, viewModel.onChangeDistanceListener)
    }

    private val oscillatorManager = OscillatorManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        collectEffect()
        if (bluetoothPermissionGranted) {
            viewModel.dispatch(MainAction.InitializeBle)
        } else {
            requestBluetoothPermission(PERMISSION_REQUEST_CODE)
        }
        lifecycle.addObserver(handDetector)
        lifecycle.addObserver(oscillatorManager)
        setContent {
            MainScreen(viewModel = viewModel)
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
                    oscillatorManager.run {
                        changeFrequency(it.frequency)
                        changeVolume(it.volume)
                    }
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
    }
}
