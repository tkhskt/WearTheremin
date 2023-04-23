package com.tkhskt.theremin.wear.theremin.di

import android.content.Context
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
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
    fun provideMessageClient(
        @ApplicationContext context: Context,
    ): MessageClient = Wearable.getMessageClient(context)

    @Provides
    @Singleton
    fun provideNodeClient(
        @ApplicationContext context: Context,
    ): NodeClient = Wearable.getNodeClient(context)
}
