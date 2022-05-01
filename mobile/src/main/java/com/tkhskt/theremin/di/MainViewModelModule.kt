package com.tkhskt.theremin.di

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tkhskt.theremin.data.repository.MessageRepository
import com.tkhskt.theremin.data.repository.MessageRepositoryImpl
import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.data.repository.ThereminRepositoryImpl
import com.tkhskt.theremin.data.source.BluetoothClient
import com.tkhskt.theremin.domain.CalcFrequencyUseCase
import com.tkhskt.theremin.domain.CalcFrequencyUseCaseImpl
import com.tkhskt.theremin.domain.CalcVolumeUseCase
import com.tkhskt.theremin.domain.CalcVolumeUseCaseImpl
import com.tkhskt.theremin.domain.GetGravityUseCase
import com.tkhskt.theremin.domain.GetGravityUseCaseImpl
import com.tkhskt.theremin.domain.OpenWearAppUseCase
import com.tkhskt.theremin.domain.OpenWearAppUseCaseImpl
import com.tkhskt.theremin.domain.SendThereminParametersUseCase
import com.tkhskt.theremin.domain.SendThereminParametersUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelModule {

    @Provides
    fun provideThereminRepository(
        bluetoothClient: BluetoothClient,
    ): ThereminRepository = ThereminRepositoryImpl(bluetoothClient)

    @Provides
    fun provideMessageRepository(
        messageClient: MessageClient,
    ): MessageRepository = MessageRepositoryImpl(messageClient)

    @Provides
    fun provideGetFrequencyUseCase(
        messageRepository: MessageRepository
    ): GetGravityUseCase = GetGravityUseCaseImpl(messageRepository)

    @Provides
    fun provideCalcFrequencyUseCase(): CalcFrequencyUseCase = CalcFrequencyUseCaseImpl()

    @Provides
    fun provideCalcVolumeUseCase(): CalcVolumeUseCase = CalcVolumeUseCaseImpl()

    @Provides
    fun provideSendThereminParametersUseCase(
        thereminRepository: ThereminRepository,
    ): SendThereminParametersUseCase = SendThereminParametersUseCaseImpl(thereminRepository)


    @Provides
    fun provideOpenWearAppUseCase(
        messageClient: MessageClient,
        nodeClient: NodeClient,
    ): OpenWearAppUseCase = OpenWearAppUseCaseImpl(messageClient, nodeClient)
}
