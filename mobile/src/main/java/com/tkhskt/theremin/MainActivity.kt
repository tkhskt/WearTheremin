package com.tkhskt.theremin

import android.Manifest
import android.Manifest.permission.BLUETOOTH
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Duration


class MainActivity : AppCompatActivity() {

    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val nodeClient by lazy { Wearable.getNodeClient(this) }

    private val viewModel by viewModels<MainViewModel>()

    private var bluetoothManager: ThereminBluetoothManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bleManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val bleAdapter = bleManager.adapter
        bluetoothManager = ThereminBluetoothManager(applicationContext, bleManager, bleAdapter)

        requestPermission()

        observeAxis()
        setContent {
            val state = viewModel.events.collectAsState()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { startWearableActivity() }
                ) {
                    Text(text = "Start Wearable")
                }
                Spacer(modifier = Modifier.size(48.dp))
                Text(text = state.value.text, color = Color.White)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        messageClient.addListener(viewModel)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    startBle()
                } else {
                    Toast.makeText(this, "Please grant permissions", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun observeAxis() {
        lifecycleScope.launch {
            viewModel.events.collect {
                bluetoothManager?.sendAxis(it.text)
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startBle()
                return
            }
            requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startBle()
                return
            }
            requestPermissions(
                arrayOf(
                    BLUETOOTH,
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startBle() {
        lifecycleScope.launch {
            bluetoothManager?.prepareBle()
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
                Log.d(TAG, "Starting activity requests sent successfully")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Starting activity failed: $exception")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"

        private const val START_ACTIVITY_PATH = "/start-activity"
        private const val COUNT_PATH = "/count"
        private const val IMAGE_PATH = "/image"
        private const val IMAGE_KEY = "photo"
        private const val TIME_KEY = "time"
        private const val COUNT_KEY = "count"
        private const val CAMERA_CAPABILITY = "camera"

        private const val PERMISSION_REQUEST_CODE = 1000

        private val countInterval = Duration.ofSeconds(5)
    }

}
