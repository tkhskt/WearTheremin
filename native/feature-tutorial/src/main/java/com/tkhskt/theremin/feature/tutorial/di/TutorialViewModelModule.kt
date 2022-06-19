package com.tkhskt.theremin.feature.tutorial.di

import com.tkhskt.theremin.feature.tutorial.ui.middleware.TutorialMiddleware
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialAction
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialEffect
import com.tkhskt.theremin.feature.tutorial.ui.model.TutorialState
import com.tkhskt.theremin.feature.tutorial.ui.reducer.TutorialReducer
import com.tkhskt.theremin.redux.Store
import com.tkhskt.theremin.redux.createStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object TutorialViewModelModule {

    @Provides
    fun provideReducer(): TutorialReducer = TutorialReducer()

    @Provides
    fun provideTutorialMiddleware(): TutorialMiddleware = TutorialMiddleware()

    @Provides
    fun provideStore(
        reducer: TutorialReducer,
        middleware: TutorialMiddleware,
    ): Store<TutorialAction, TutorialState, TutorialEffect> = createStore(
        reducer = reducer,
        initialState = TutorialState.INITIAL,
        middlewares = listOf(middleware),
    )
}
