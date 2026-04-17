package com.dreifus.app.features.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreifus.app.counter.data.CounterRepository
import com.dreifus.app.features.counter.mvu.CounterState
import com.dreifus.app.features.counter.mvu.CounterUpdate
import com.dreifus.app.features.counter.mvu.LoadIncrementHandler
import com.yavorcool.mvucore.impl.Store
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Inject
@ViewModelKey(CounterViewModel::class)
@ContributesIntoMap(AppScope::class)
class CounterViewModel(repo: CounterRepository) : ViewModel() {
    val store = Store(
        initialState = CounterState(),
        update = CounterUpdate,
        commandHandlers = listOf(LoadIncrementHandler(repo)),
    )

    init {
        store.launch(viewModelScope)
    }
}
