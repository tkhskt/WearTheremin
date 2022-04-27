package com.tkhskt.theremin.di

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import com.tkhskt.theremin.data.BluetoothClient
import com.tkhskt.theremin.data.ThereminRepository
import com.tkhskt.theremin.data.ThereminRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelModule {

    @Provides
    fun provideBluetoothManager(
        @ApplicationContext context: Context,
    ): BluetoothManager = context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

    @Provides
    fun provideBluetoothClient(
        @ApplicationContext context: Context,
        bluetoothManager: BluetoothManager,
    ): BluetoothClient = BluetoothClient(
        context = context,
        bleManager = bluetoothManager,
    )

    @Provides
    fun provideThereminRepository(
        bluetoothClient: BluetoothClient,
    ): ThereminRepository = ThereminRepositoryImpl(bluetoothClient)
}
