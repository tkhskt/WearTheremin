package com.tkhskt.theremin.infra.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.tkhskt.theremin.domain.artifacts.repository.ArtifactsRepository
import com.tkhskt.theremin.domain.audio.repository.AudioRepository
import com.tkhskt.theremin.infra.artifacts.repositoryimpl.ArtifactsRepositoryImpl
import com.tkhskt.theremin.infra.audio.repositoryimpl.AudioRepositoryImpl
import com.tkhskt.theremin.infra.bluetooth.BluetoothClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object InfraModule {

    @Provides
    fun provideThereminRepository(
        bluetoothClient: BluetoothClient,
    ): AudioRepository = AudioRepositoryImpl(bluetoothClient)

    @Provides
    fun provideArtifactsRepository(
        @ApplicationContext context: Context,
        moshi: Moshi,
    ): ArtifactsRepository = ArtifactsRepositoryImpl(context, moshi)
}
