package com.tkhskt.theremin.di

import android.bluetooth.BluetoothManager
import android.content.Context
import com.tkhskt.theremin.data.source.BluetoothClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

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
}
