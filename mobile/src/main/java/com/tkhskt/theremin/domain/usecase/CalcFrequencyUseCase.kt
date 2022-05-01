package com.tkhskt.theremin.domain.usecase

import androidx.core.math.MathUtils

interface CalcFrequencyUseCase {
    operator fun invoke(gravity: Float): Float
}

class CalcFrequencyUseCaseImpl : CalcFrequencyUseCase {

    override fun invoke(gravity: Float): Float {
        val freq = MIN_FREQ + (gravity + 4f) * 50.49f
        return MathUtils.clamp(freq, MIN_FREQ, MAX_FREQ)
    }

    companion object {
        private const val MIN_FREQ = 269.292f
        private const val MAX_FREQ = 1077.167f
    }
}
