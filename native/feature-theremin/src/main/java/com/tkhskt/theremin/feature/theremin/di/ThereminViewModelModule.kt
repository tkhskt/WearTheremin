package com.tkhskt.theremin.feature.theremin.di

import com.tkhskt.theremin.domain.audio.repository.MessageRepository
import com.tkhskt.theremin.domain.audio.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.domain.audio.usecase.CalcFrequencyUseCaseImpl
import com.tkhskt.theremin.domain.audio.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.domain.audio.usecase.CalcVolumeUseCaseImpl
import com.tkhskt.theremin.domain.audio.usecase.GetGravityUseCase
import com.tkhskt.theremin.domain.audio.usecase.GetGravityUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object ThereminViewModelModule {

    @Provides
    fun provideGetFrequencyUseCase(
        messageRepository: MessageRepository,
    ): GetGravityUseCase = GetGravityUseCaseImpl(Dispatchers.IO, messageRepository)

    @Provides
    fun provideCalcFrequencyUseCase(): CalcFrequencyUseCase = CalcFrequencyUseCaseImpl()

    @Provides
    fun provideCalcVolumeUseCase(): CalcVolumeUseCase = CalcVolumeUseCaseImpl()
}
