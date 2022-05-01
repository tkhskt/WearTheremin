package com.tkhskt.theremin

import android.content.pm.PackageManager
import android.os.Bundle
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
import com.tkhskt.theremin.ui.model.MainEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val handDetector = HandDetector(this)

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectEvent()
        if (bluetoothPermissionGranted) {
            viewModel.dispatchEvent(MainEvent.InitializeBle)
        } else {
            requestBluetoothPermission(PERMISSION_REQUEST_CODE)
        }
        lifecycle.addObserver(handDetector)
        setContent {
            MainScreen(viewModel = viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        handDetector.init(viewModel.onChangeDistanceListener)
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
                viewModel.dispatchEvent(MainEvent.InitializeBle)
            } else {
                Toast.makeText(this, "Please grant permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun collectEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.events.collect { event ->
                    if (event is MainEvent.ClickCameraButton) {
                        handDetector.setupStreamingModePipeline()
                    }
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
    }
}
