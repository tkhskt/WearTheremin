package com.tkhskt.theremin.infra.di

import android.bluetooth.BluetoothManager
import android.content.Context
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tkhskt.theremin.domain.artifacts.repository.ArtifactsRepository
import com.tkhskt.theremin.domain.audio.repository.AudioRepository
import com.tkhskt.theremin.domain.audio.repository.MessageRepository
import com.tkhskt.theremin.infra.artifacts.repositoryimpl.ArtifactsRepositoryImpl
import com.tkhskt.theremin.infra.audio.repositoryimpl.AudioRepositoryImpl
import com.tkhskt.theremin.infra.bluetooth.BluetoothClient
import com.tkhskt.theremin.infra.message.repositoryimpl.MessageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InfraModule {

    @Provides
    @Singleton
    fun provideMessageClient(
        @ApplicationContext context: Context,
    ): MessageClient = Wearable.getMessageClient(context)

    @Provides
    @Singleton
    fun provideNodeClient(
        @ApplicationContext context: Context,
    ): NodeClient = Wearable.getNodeClient(context)

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideBluetoothClient(
        @ApplicationContext context: Context,
    ): BluetoothClient {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return BluetoothClient(
            context = context,
            bleManager = manager,
        )
    }

    @Singleton
    @Provides
    fun provideThereminRepository(
        bluetoothClient: BluetoothClient,
    ): AudioRepository = AudioRepositoryImpl(bluetoothClient)

    @Singleton
    @Provides
    fun provideArtifactsRepository(
        @ApplicationContext context: Context,
        moshi: Moshi,
    ): ArtifactsRepository = ArtifactsRepositoryImpl(context, moshi)

    @Singleton
    @Provides
    fun provideMessageRepository(
        messageClient: MessageClient,
    ): MessageRepository = MessageRepositoryImpl(messageClient)
}
