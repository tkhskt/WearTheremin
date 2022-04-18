package com.tkhskt.theremin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import com.tkhskt.theremin.ui.MainScreen
import com.tkhskt.theremin.ui.MainViewModel
import com.tkhskt.theremin.ui.model.MainEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
                MainScreen(viewModel)
            }
        })
        viewModel.dispatchEvent(MainEvent.Initialize)
        ambientController = AmbientModeSupport.attach(this)
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = ThereminAmbientCallback()

    private class ThereminAmbientCallback : AmbientModeSupport.AmbientCallback() {

        override fun onEnterAmbient(ambientDetails: Bundle?) {
            // Handle entering ambient mode
        }

        override fun onExitAmbient() {
            // Handle exiting ambient mode
        }

        override fun onUpdateAmbient() {
            // Update the content
        }
    }
}
