package com.tkhskt.theremin.redux

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge

interface State

interface UiState

interface Action

interface SideEffect

interface Reducer<ACTION : Action, STATE : State> {
    suspend fun reduce(action: ACTION, state: STATE): STATE
}

abstract class ReduxViewModel<in ACTION : Action, out UI_STATE : UiState, SIDE_EFFECT : SideEffect> :
    ViewModel() {
    abstract val uiState: StateFlow<UI_STATE>
    abstract val sideEffect: SharedFlow<SIDE_EFFECT>
    abstract fun dispatch(action: ACTION)
}


interface Middleware<ACTION : Action, STATE : State, SIDE_EFFECT : SideEffect> {
    val sideEffect: SharedFlow<SIDE_EFFECT>?
    suspend fun dispatch(store: Store<ACTION, STATE, SIDE_EFFECT>): (suspend (ACTION) -> Unit) -> (suspend (ACTION) -> Unit)
}

@Suppress("UNCHECKED_CAST")
class Store<ACTION : Action, STATE : State, SIDE_EFFECT : SideEffect>(
    initialState: STATE,
    private val reducer: Reducer<ACTION, STATE>,
    private val middlewares: List<Middleware<ACTION, STATE, SIDE_EFFECT>>,
) {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    val currentState: STATE
        get() = state.value

    val sideEffect: Flow<SIDE_EFFECT>
        get() = middlewares.mapNotNull { it.sideEffect }.merge()

    val dispatch: suspend (ACTION) -> Unit = { action ->
        compose(middlewares.map { it.dispatch(this) })(::dispatch)(action)
    }

    private suspend fun dispatch(action: ACTION) {
        _state.value = reducer.reduce(action, _state.value)
    }

    private fun compose(functions: List<(suspend (ACTION) -> Unit) -> (suspend (ACTION) -> Unit)>): (suspend (ACTION) -> Unit) -> (suspend (ACTION) -> Unit) {
        return { last ->
            { action ->
                functions.foldRight(last) { f, composed -> f(composed) }(action)
            }
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
