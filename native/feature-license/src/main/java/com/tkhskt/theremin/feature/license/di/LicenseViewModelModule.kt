package com.tkhskt.theremin.feature.license.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tkhskt.theremin.domain.artifacts.usecase.GetArtifactsUseCase
import com.tkhskt.theremin.domain.artifacts.usecase.GetArtifactsUseCaseImpl
import com.tkhskt.theremin.domain.artifacts.repository.ArtifactsRepository
import com.tkhskt.theremin.domain.artifacts.repository.ArtifactsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object LicenseViewModelModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideArtifactsRepository(
        @ApplicationContext context: Context,
        moshi: Moshi,
    ): ArtifactsRepository = ArtifactsRepositoryImpl(context, moshi)

    @Provides
    fun provideGetArtifactsUseCase(
        artifactsRepository: ArtifactsRepository,
    ): GetArtifactsUseCase = GetArtifactsUseCaseImpl(artifactsRepository)
}
