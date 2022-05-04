package com.tkhskt.theremin.di

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.data.repository.ThereminRepositoryImpl
import com.tkhskt.theremin.domain.SendGravityUseCase
import com.tkhskt.theremin.domain.SendGravityUseCaseImpl
import com.tkhskt.theremin.redux.Store
import com.tkhskt.theremin.redux.createStore
import com.tkhskt.theremin.ui.middleware.NetworkMiddleware
import com.tkhskt.theremin.ui.middleware.SensorMiddleware
import com.tkhskt.theremin.ui.model.MainAction
import com.tkhskt.theremin.ui.model.MainEffect
import com.tkhskt.theremin.ui.model.MainState
import com.tkhskt.theremin.ui.reducer.MainReducer
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

    @Provides
    fun provideReducer(): MainReducer = MainReducer()

    @Provides
    fun provideNetworkMiddleware(
        sendGravityUseCase: SendGravityUseCase,
    ): NetworkMiddleware = NetworkMiddleware(
        sendGravityUseCase = sendGravityUseCase,
    )

    @Provides
    fun provideSensorMiddleware(): SensorMiddleware = SensorMiddleware()

    @Provides
    fun provideStore(
        reducer: MainReducer,
        networkMiddleware: NetworkMiddleware,
        sensorMiddleware: SensorMiddleware,
    ): Store<MainAction, MainState, MainEffect> = createStore(
        reducer = reducer,
        initialState = MainState.INITIAL,
        middlewares = listOf(networkMiddleware, sensorMiddleware),
    )
}
