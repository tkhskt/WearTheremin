package com.tkhskt.theremin.domain.usecase

import androidx.core.math.MathUtils

interface CalcVolumeUseCase {
    operator fun invoke(distance: Float): Float
}

class CalcVolumeUseCaseImpl : CalcVolumeUseCase {
    override fun invoke(distance: Float): Float {
        return 1.0f - MathUtils.clamp(
            distance - 0.7f,
            MIN_DISTANCE,
            MAX_DISTANCE,
        ) * MAX_DISTANCE_RECIPROCAL
    }

    companion object {
        private const val MIN_DISTANCE = 0f
        private const val MAX_DISTANCE = 4f / 10f
        private const val MAX_DISTANCE_RECIPROCAL = 10f / 4f
    }
}
