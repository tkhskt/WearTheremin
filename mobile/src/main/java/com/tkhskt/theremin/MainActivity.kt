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
import com.google.android.gms.wearable.Wearable
import com.tkhskt.theremin.ui.HandDetector
import com.tkhskt.theremin.ui.MainScreen
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.model.MainEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val handDetector = HandDetector()

    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val nodeClient by lazy { Wearable.getNodeClient(this) }

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
        messageClient.addListener(viewModel)
        handDetector.init(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    when (it) {
                        is MainEvent.ClickStartWearableButton -> {
                            startWearableActivity()
                        }
                        is MainEvent.ClickCameraButton -> {
                            handDetector.setupStreamingModePipeline(this@MainActivity)
                        }
                        else -> {
                            // no-op
                        }
                    }
                }
            }
        }
    }

    private fun startWearableActivity() {
        lifecycleScope.launch {
            try {
                val nodes = nodeClient.connectedNodes.await()
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(node.id, START_ACTIVITY_PATH, byteArrayOf())
                            .await()
                    }
                }.awaitAll()
                Timber.d(TAG, "Starting activity requests sent successfully")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Timber.d(TAG, "Starting activity failed: $exception")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"

        private const val START_ACTIVITY_PATH = "/start-activity"

        private const val PERMISSION_REQUEST_CODE = 1000
    }
}
