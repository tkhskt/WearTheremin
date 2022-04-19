package com.tkhskt.theremin.di

import android.app.Service
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
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
    fun provideNodeClient(@ApplicationContext context: Context): NodeClient =
        Wearable.getNodeClient(context)

    @Provides
    fun provideMessageClient(@ApplicationContext context: Context): MessageClient =
        Wearable.getMessageClient(context)

    @Provides
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager =
        context.getSystemService(
            Service.SENSOR_SERVICE
        ) as SensorManager

    @Provides
    fun provideSensor(manager: SensorManager): Sensor =
        manager.getDefaultSensor(Sensor.TYPE_GRAVITY)

    @Provides
    fun provideThereminRepository(
        messageClient: MessageClient,
        nodeClient: NodeClient,
    ): ThereminRepository = ThereminRepositoryImpl(
        messageClient = messageClient,
        nodeClient = nodeClient,
    )
}
