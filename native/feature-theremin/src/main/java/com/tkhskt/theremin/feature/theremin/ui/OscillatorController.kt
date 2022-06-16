package com.tkhskt.theremin.feature.theremin.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class OscillatorController : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        createStream()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        playSound(true)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        playSound(false)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        destroyStream()
    }

    external fun playSound(enable: Boolean): Int

    external fun changeFrequency(frequency: Float)

    external fun changeVolume(volume: Float)

    private external fun createStream(): Int

    private external fun destroyStream()

    companion object {
        init {
            System.loadLibrary("theremin")
        }
    }
}
