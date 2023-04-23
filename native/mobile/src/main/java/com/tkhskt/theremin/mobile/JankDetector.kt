package com.tkhskt.theremin.mobile

import android.view.Window
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.PerformanceMetricsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import timber.log.Timber

class JankDetector : DefaultLifecycleObserver {

    private lateinit var jankStats: JankStats

    private val jankFrameListener = JankStats.OnFrameListener { frameData ->
        if (frameData.isJank) {
            Timber.w(frameData.toString())
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        jankStats.isTrackingEnabled = true
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        jankStats.isTrackingEnabled = false
    }

    fun startDetection(stateName: String, window: Window) {
        val metricsStateHolder = PerformanceMetricsState.getForHierarchy(window.decorView)
        jankStats = JankStats.createAndTrack(
            window,
            Dispatchers.Default.asExecutor(),
            jankFrameListener,
        )
        metricsStateHolder.state?.addState(stateName, javaClass.simpleName)
    }
}
