package com.tkhskt.theremin.domain.usecase

import androidx.core.math.MathUtils

interface CalcVolumeUseCase {
    operator fun invoke(distance: Float): Float
}

class CalcVolumeUseCaseImpl : CalcVolumeUseCase {

    override fun invoke(distance: Float): Float {
        return MathUtils.clamp(distance, MIN_DISTANCE, MAX_DISTANCE).let {
            it * (5f / 3f)
        }
    }

    companion object {
        private const val MIN_DISTANCE = 0f
        private const val MAX_DISTANCE = 6f
    }
}
