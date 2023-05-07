package com.tkhskt.theremin.wear.theremin.di

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tkhskt.theremin.wear.domain.SendGravityUseCase
import com.tkhskt.theremin.wear.domain.SendGravityUseCaseImpl
import com.tkhskt.theremin.wear.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.wear.theremin.data.repository.ThereminRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelModule {

    @Provides
    fun provideThereminRepository(
        messageClient: MessageClient,
        nodeClient: NodeClient,
    ): ThereminRepository = ThereminRepositoryImpl(
        messageClient = messageClient,
        nodeClient = nodeClient,
    )

    @Provides
    fun provideSendGravityUseCase(
        thereminRepository: ThereminRepository,
    ): SendGravityUseCase = SendGravityUseCaseImpl(thereminRepository)
}
