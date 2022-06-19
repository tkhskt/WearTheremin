package com.tkhskt.theremin.redux

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge

interface State

interface UiState

interface Action

interface SideEffect

interface Reducer<ACTION : Action, STATE : State> {
    suspend fun reduce(action: ACTION, state: STATE): STATE
}

interface Middleware<ACTION : Action, STATE : State, SIDE_EFFECT : SideEffect> {
    val sideEffect: SharedFlow<SIDE_EFFECT>?

    suspend fun dispatchBeforeReduce(action: ACTION, state: STATE): ACTION

    suspend fun dispatchAfterReduce(action: ACTION, state: STATE): ACTION
}

abstract class ReduxViewModel<in ACTION : Action, out UI_STATE : UiState, SIDE_EFFECT : SideEffect> :
    ViewModel() {
    abstract val uiState: StateFlow<UI_STATE>
    abstract val sideEffect: SharedFlow<SIDE_EFFECT>
    abstract fun dispatch(action: ACTION)
}

class Store<ACTION : Action, STATE : State, SIDE_EFFECT : SideEffect>(
    initialState: STATE,
    private val reducer: Reducer<ACTION, STATE>,
    private val middlewares: List<Middleware<ACTION, STATE, SIDE_EFFECT>>,
) {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    val sideEffect: Flow<SIDE_EFFECT>
        get() = middlewares.mapNotNull { it.sideEffect }.merge()

    suspend fun dispatch(action: ACTION) {
        var newAction = action
        middlewares.forEach {
            newAction = it.dispatchBeforeReduce(newAction, state.value)
        }
        _state.value = reducer.reduce(newAction, state.value)
        middlewares.forEach {
            newAction = it.dispatchAfterReduce(newAction, state.value)
        }
    }
}

fun <ACTION : Action, STATE : State, SIDE_EFFECT : SideEffect> createStore(
    reducer: Reducer<ACTION, STATE>,
    initialState: STATE,
    middlewares: List<Middleware<ACTION, STATE, SIDE_EFFECT>> = emptyList(),
): Store<ACTION, STATE, SIDE_EFFECT> {
    return Store(initialState, reducer, middlewares)
}

fun <ACTION : Action, STATE : State> combineReducers(
    vararg reducers: Reducer<ACTION, STATE>,
): Reducer<ACTION, STATE> {
    return object : Reducer<ACTION, STATE> {
        override suspend fun reduce(action: ACTION, state: STATE): STATE {
            var outState = state
            reducers.forEach {
                outState = it.reduce(action, outState)
            }
            return outState
        }
    }
}
