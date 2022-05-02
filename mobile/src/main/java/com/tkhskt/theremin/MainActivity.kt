package com.tkhskt.theremin

import android.content.pm.PackageManager
import android.media.AudioTrack
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
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val handDetector by lazy {
        HandDetector(this, viewModel.onChangeDistanceListener)
    }

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
        setContent {
            MainScreen(viewModel = viewModel)
        }
        createStream()
        playSound(true)
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
                viewModel.viewState.collect {
                    changeFrequency(it.frequency)
                    changeVolume(it.volume)
                }
            }
        }
    }

    // Creates and starts Oboe stream to play audio
    private external fun createStream(): Int

    // Closes and destroys Oboe stream when app goes out of focus
    private external fun destroyStream()

    // Plays sound on user tap
    private external fun playSound(enable: Boolean): Int

    private external fun changeFrequency(frequency: Float)

    private external fun changeVolume(volume: Float)

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000

        init {
            System.loadLibrary("theremin")
        }
    }
}
