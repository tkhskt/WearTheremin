package com.tkhskt.theremin.di

import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.data.repository.ThereminRepositoryImpl
import com.tkhskt.theremin.data.source.BluetoothClient
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
}
