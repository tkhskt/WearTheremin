package com.tkhskt.theremin.data.repository

interface AudioRepository {
    fun getVolume(): Float
    fun saveVolume(volume: Float)
}

class AudioRepositoryImpl : AudioRepository {

    private var volume = 0f

    override fun getVolume(): Float {
        return volume
    }

    override fun saveVolume(volume: Float) {
        this.volume = volume
    }
}
