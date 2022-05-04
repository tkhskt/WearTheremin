package com.tkhskt.theremin.domain.usecase

import com.tkhskt.theremin.data.repository.AudioRepository

interface GetVolumeUseCase {
    operator fun invoke(): Float
}

class GetVolumeUseCaseImpl(
    private val audioRepository: AudioRepository,
) : GetVolumeUseCase {

    override fun invoke(): Float {
        return audioRepository.getVolume()
    }
}