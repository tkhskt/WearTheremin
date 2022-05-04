package com.tkhskt.theremin.domain

import com.tkhskt.theremin.data.repository.DistanceRepository

interface GetPositionUseCase {
    fun getPosition(ay: Float): Float
}

// not used
class GetPositionUseCaseImpl(
    private val distanceRepository: DistanceRepository,
) : GetPositionUseCase {

    override fun getPosition(ay: Float): Float {
        val prevTime = distanceRepository.getTime()
        val prevVy = distanceRepository.getVy()
        val prevDistance = distanceRepository.getDistance()

        val newTime = prevTime + 1
        val vy1 = prevVy + ay * 1f
        val vy01 = (prevVy + vy1) / 2f
        val newDistance = prevDistance + vy01 * 1f

//        val newPosition = distanceRepository.getPosition() + newDistance

        distanceRepository.run {
            saveTime(newTime)
            saveVy(vy1)
            saveDistance(newDistance)
//            savePosition(newPosition)
        }
        return newDistance
    }
}
