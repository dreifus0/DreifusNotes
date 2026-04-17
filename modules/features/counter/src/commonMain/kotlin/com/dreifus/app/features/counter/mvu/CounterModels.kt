package com.dreifus.app.features.counter.mvu

import com.dreifus.arch.lce.LceState

data class CounterState(
    val countState: LceState<Int> = LceState.Content(0),
)

sealed interface CounterEvent {
    data object Increment : CounterEvent
    data object Decrement : CounterEvent
    data object AsyncIncrement : CounterEvent
    data class AsyncResult(val newValue: Int) : CounterEvent
    data class AsyncError(val error: Throwable) : CounterEvent
    data object OpenDetail : CounterEvent
}

sealed interface CounterCommand {
    data class LoadIncrement(val currentValue: Int) : CounterCommand
}

sealed interface CounterEffect {
    data class ShowMessage(val message: String) : CounterEffect
    data object NavigateToDetail : CounterEffect
}
