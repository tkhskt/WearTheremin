package com.tkhskt.theremin.di

import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tkhskt.theremin.data.repository.MessageRepository
import com.tkhskt.theremin.data.repository.MessageRepositoryImpl
import com.tkhskt.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.data.repository.ThereminRepositoryImpl
import com.tkhskt.theremin.data.source.BluetoothClient
import com.tkhskt.theremin.domain.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.domain.usecase.CalcFrequencyUseCaseImpl
import com.tkhskt.theremin.domain.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.domain.usecase.CalcVolumeUseCaseImpl
import com.tkhskt.theremin.domain.usecase.GetGravityUseCase
import com.tkhskt.theremin.domain.usecase.GetGravityUseCaseImpl
import com.tkhskt.theremin.domain.usecase.OpenWearAppUseCase
import com.tkhskt.theremin.domain.usecase.OpenWearAppUseCaseImpl
import com.tkhskt.theremin.domain.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.domain.usecase.SendThereminParametersUseCaseImpl
import com.tkhskt.theremin.redux.Store
import com.tkhskt.theremin.redux.createStore
import com.tkhskt.theremin.ui.middleware.MainMiddleware
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
        bluetoothClient: BluetoothClient,
    ): ThereminRepository = ThereminRepositoryImpl(bluetoothClient)

    @Provides
    fun provideMessageRepository(
        messageClient: MessageClient,
    ): MessageRepository = MessageRepositoryImpl(messageClient)

    @Provides
    fun provideGetFrequencyUseCase(
        messageRepository: MessageRepository,
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

    @Provides
    fun provideReducer(): MainReducer = MainReducer()

    @Provides
    fun provideMainMiddleware(
        thereminRepository: ThereminRepository,
        sendThereminParametersUseCase: SendThereminParametersUseCase,
        calcFrequencyUseCase: CalcFrequencyUseCase,
        calcVolumeUseCase: CalcVolumeUseCase,
        openWearAppUseCase: OpenWearAppUseCase,
    ): MainMiddleware = MainMiddleware(
        thereminRepository = thereminRepository,
        sendThereminParametersUseCase = sendThereminParametersUseCase,
        calcFrequencyUseCase = calcFrequencyUseCase,
        calcVolumeUseCase = calcVolumeUseCase,
        openWearAppUseCase = openWearAppUseCase
    )

    @Provides
    fun provideStore(
        reducer: MainReducer,
        middleware: MainMiddleware,
    ): Store<MainAction, MainState, MainEffect> = createStore(
        reducer = reducer,
        initialState = MainState.INITIAL,
        middlewares = listOf(middleware),
    )
}
