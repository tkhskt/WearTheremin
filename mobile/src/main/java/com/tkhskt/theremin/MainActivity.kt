package com.tkhskt.theremin

import android.Manifest
import android.Manifest.permission.BLUETOOTH
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
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
import com.google.mediapipe.framework.TextureFrame
import com.google.mediapipe.solutioncore.CameraInput
import com.google.mediapipe.solutions.hands.HandLandmark
import com.google.mediapipe.solutions.hands.Hands
import com.google.mediapipe.solutions.hands.HandsOptions
import com.google.mediapipe.solutions.hands.HandsResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : AppCompatActivity() {

    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val nodeClient by lazy { Wearable.getNodeClient(this) }

    private val viewModel by viewModels<MainViewModel>()

    private var bluetoothManager: ThereminBluetoothManager? = null

    private var hands: Hands? = null

    // Live camera demo UI and camera components.
    private var cameraInput: CameraInput? = null

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
                Button(
                    onClick = {
                        stopCurrentPipeline()
                        setupStreamingModePipeline()
                    }
                ) {
                    Text(text = "Start Camera")
                }
                Spacer(modifier = Modifier.size(48.dp))
                Text(text = state.value.text, color = Color.White)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        messageClient.addListener(viewModel)
        // Restarts the camera and the opengl surface rendering.
        cameraInput = CameraInput(this).apply {
            setNewFrameListener { textureFrame: TextureFrame? ->
                hands?.send(
                    textureFrame
                )
            }
        }
        startCamera()
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

    /** Sets up core workflow for streaming mode.  */
    private fun setupStreamingModePipeline() {
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands = Hands(
            this,
            HandsOptions.builder()
                .setStaticImageMode(false)
                .setMaxNumHands(2)
                .setRunOnGpu(RUN_ON_GPU)
                .build()
        )
        hands?.setErrorListener { message: String, e: RuntimeException? ->
            Log.e(
                TAG,
                "MediaPipe Hands error:$message"
            )
        }
        cameraInput = CameraInput(this)
        cameraInput?.setNewFrameListener { textureFrame: TextureFrame? ->
            hands?.send(
                textureFrame
            )
        }
        hands?.setResultListener { handsResult: HandsResult ->
            logWristLandmark(handsResult,  /*showPixelValues=*/false)
        }
        startCamera()
    }

    private fun startCamera() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowMetrics = wm.currentWindowMetrics
        val windowInsets: WindowInsets = windowMetrics.windowInsets

        val insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
        )
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom

        val b = windowMetrics.bounds
        val width = b.width() - insetsWidth
        val height = b.height() - insetsHeight

        val hands = hands ?: return
        cameraInput?.start(
            this,
            hands.glContext,
            CameraInput.CameraFacing.FRONT,
            width,
            height
        )
    }

    private fun stopCurrentPipeline() {
        cameraInput?.run {
            setNewFrameListener(null)
            close()
        }
        hands?.close()
    }

    private fun logWristLandmark(result: HandsResult, showPixelValues: Boolean) {
        if (result.multiHandLandmarks().isEmpty()) {
            return
        }
        val wristLandmark = result.multiHandLandmarks()[0].landmarkList[HandLandmark.WRIST]
        // For Bitmaps, show the pixel values. For texture inputs, show the normalized coordinates.
        if (showPixelValues) {
            val width = result.inputBitmap().width
            val height = result.inputBitmap().height
            Log.i(
                TAG, String.format(
                    "MediaPipe Hand wrist coordinates (pixel values): x=%f, y=%f",
                    wristLandmark.x * width, wristLandmark.y * height
                )
            )
        } else {
            Log.i(
                TAG, String.format(
                    "MediaPipe Hand wrist normalized coordinates (value range: [0, 1]): x=%f, y=%f",
                    wristLandmark.x, wristLandmark.y
                )
            )
        }
        if (result.multiHandWorldLandmarks().isEmpty()) {
            return
        }
        val wristWorldLandmark =
            result.multiHandLandmarks()[0].landmarkList[HandLandmark.WRIST]
        Log.i(
            TAG, String.format(
                "MediaPipe Hand wrist world coordinates (in meters with the origin at the hand's"
                        + " approximate geometric center): x=%f m, y=%f m, z=%f m",
                wristWorldLandmark.x, wristWorldLandmark.y, wristWorldLandmark.z
            )
        )
    }

    companion object {
        private const val TAG = "MainActivity"

        private const val START_ACTIVITY_PATH = "/start-activity"

        private const val PERMISSION_REQUEST_CODE = 1000

        private const val RUN_ON_GPU = true
    }

}
