package com.tkhskt.theremin.data

interface DistanceRepository {
    fun getTime(): Int
    fun getVy(): Float
    fun getDistance(): Float
    fun getPosition(): Float

    fun saveTime(time: Int)
    fun saveVy(vy: Float)
    fun saveDistance(distance: Float)

    fun savePosition(position: Float)
}

class DistanceRepositoryImpl : DistanceRepository {

    private var time = 0
    private var vy = 0f
    private var distance = 0f
    private var position = 0f

    override fun getTime(): Int = time

    override fun getVy(): Float = vy

    override fun getDistance(): Float = distance

    override fun getPosition(): Float = position

    override fun saveTime(time: Int) {
        this.time = time
    }

    override fun saveVy(vy: Float) {
        this.vy = vy
    }

    override fun saveDistance(distance: Float) {
        this.distance = distance
    }

    override fun savePosition(position: Float) {
        this.position = position
    }
}
