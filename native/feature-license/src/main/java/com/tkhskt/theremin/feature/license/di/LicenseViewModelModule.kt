package com.tkhskt.theremin.feature.license.di

import com.tkhskt.theremin.domain.artifacts.repository.ArtifactsRepository
import com.tkhskt.theremin.domain.artifacts.usecase.GetArtifactsUseCase
import com.tkhskt.theremin.domain.artifacts.usecase.GetArtifactsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object LicenseViewModelModule {

    @Provides
    fun provideGetArtifactsUseCase(
        artifactsRepository: ArtifactsRepository,
    ): GetArtifactsUseCase = GetArtifactsUseCaseImpl(artifactsRepository)
}
