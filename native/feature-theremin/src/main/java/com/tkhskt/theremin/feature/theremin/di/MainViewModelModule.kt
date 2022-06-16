package com.tkhskt.theremin.feature.theremin.di

import com.google.android.gms.wearable.MessageClient
import com.tkhskt.theremin.feature.theremin.data.repository.MessageRepository
import com.tkhskt.theremin.feature.theremin.data.repository.MessageRepositoryImpl
import com.tkhskt.theremin.feature.theremin.data.repository.ThereminRepository
import com.tkhskt.theremin.feature.theremin.data.repository.ThereminRepositoryImpl
import com.tkhskt.theremin.feature.theremin.data.source.BluetoothClient
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcFrequencyUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcFrequencyUseCaseImpl
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcVolumeUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.CalcVolumeUseCaseImpl
import com.tkhskt.theremin.feature.theremin.domain.usecase.GetGravityUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.GetGravityUseCaseImpl
import com.tkhskt.theremin.feature.theremin.domain.usecase.SendThereminParametersUseCase
import com.tkhskt.theremin.feature.theremin.domain.usecase.SendThereminParametersUseCaseImpl
import com.tkhskt.theremin.feature.theremin.ui.middleware.MainMiddleware
import com.tkhskt.theremin.feature.theremin.ui.model.MainAction
import com.tkhskt.theremin.feature.theremin.ui.model.MainEffect
import com.tkhskt.theremin.feature.theremin.ui.model.MainState
import com.tkhskt.theremin.feature.theremin.ui.reducer.MainReducer
import com.tkhskt.theremin.redux.Store
import com.tkhskt.theremin.redux.createStore
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
    fun provideReducer(): MainReducer = MainReducer()

    @Provides
    fun provideMainMiddleware(
        thereminRepository: ThereminRepository,
        sendThereminParametersUseCase: SendThereminParametersUseCase,
        calcFrequencyUseCase: CalcFrequencyUseCase,
        calcVolumeUseCase: CalcVolumeUseCase,
    ): MainMiddleware = MainMiddleware(
        thereminRepository = thereminRepository,
        sendThereminParametersUseCase = sendThereminParametersUseCase,
        calcFrequencyUseCase = calcFrequencyUseCase,
        calcVolumeUseCase = calcVolumeUseCase,
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
