package com.tkhskt.theremin.domain.usecase

import com.tkhskt.theremin.data.repository.AudioRepository

interface SaveVolumeUseCase {
    operator fun invoke(volume: Float)
}

class SaveVolumeUseCaseImpl(
    private val audioRepository: AudioRepository,
) : SaveVolumeUseCase {

    override fun invoke(volume: Float) {
        audioRepository.saveVolume(volume)
    }
}
