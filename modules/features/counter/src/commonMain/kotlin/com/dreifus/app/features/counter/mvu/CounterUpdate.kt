package com.dreifus.app.features.counter.mvu

import com.dreifus.arch.lce.LceState
import com.dreifus.arch.lce.value
import com.yavorcool.mvucore.Next
import com.yavorcool.mvucore.Update

val CounterUpdate = Update<CounterState, CounterEvent, CounterCommand, CounterEffect> { state, event ->
    val currentValue = state.countState.value ?: 0
    when (event) {
        is CounterEvent.Increment -> Next(
            state = state.copy(countState = LceState.Content(currentValue + 1)),
        )

        is CounterEvent.Decrement -> Next(
            state = state.copy(countState = LceState.Content(currentValue - 1)),
        )

        is CounterEvent.AsyncIncrement -> Next(
            state = state.copy(countState = LceState.Loading.Initial),
            command = CounterCommand.LoadIncrement(currentValue),
        )

        is CounterEvent.AsyncResult -> Next(
            state = state.copy(countState = LceState.Content(event.newValue)),
            effect = CounterEffect.ShowMessage("Async increment done! New value: ${event.newValue}"),
        )

        is CounterEvent.AsyncError -> Next(
            state = state.copy(countState = LceState.Error(event.error)),
        )

        is CounterEvent.OpenDetail -> Next(
            state = state,
            effect = CounterEffect.NavigateToDetail,
        )
    }
}
