package com.tkhskt.theremin.feature.theremin.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class OscillatorController : DefaultLifecycleObserver {

    private var isPlaying = false

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        createStream()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        playSound(isPlaying)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        playSound(false)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        destroyStream()
    }

    fun play() {
        playSound(true)
        isPlaying = true
    }

    fun pause() {
        playSound(false)
        isPlaying = false
    }

    external fun changeFrequency(frequency: Float)

    external fun changeVolume(volume: Float)

    private external fun playSound(enable: Boolean): Int

    private external fun createStream(): Int

    private external fun destroyStream()

    companion object {
        init {
            System.loadLibrary("theremin")
        }
    }
}
