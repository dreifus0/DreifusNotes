package com.dreifus.app.counter.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.delay

@Inject
@SingleIn(AppScope::class)
class CounterRepository {
    suspend fun incrementRemote(currentValue: Int): Int {
        delay(1000) // simulate network
        return currentValue + 1
    }
}
