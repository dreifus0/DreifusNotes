package com.dreifus.arch.lce

import com.dreifus.arch.lce.LceState.*

sealed class LceState<out T : Any> {

    sealed class Loading<out T : Any> : LceState<T>() {
        data object Initial : Loading<Nothing>()
    }

    data class Content<out T : Any>(
        val value: T,
        val isRefreshing: Boolean = false
    ) : LceState<T>()

    data class Error(
        val error: Throwable? = null,
        val isRefreshing: Boolean = false
    ) : LceState<Nothing>()
}

val LceState<*>.isLoading: Boolean
    get() = when (this) {
        is Loading -> true
        is Content -> isRefreshing
        is Error -> isRefreshing
    }

val LceState<*>.isRefreshing: Boolean
    get() = when (this) {
        is Loading -> false
        is Content -> isRefreshing
        is Error -> isRefreshing
    }

val <T : Any> LceState<T>.value: T?
    get() = when (this) {
        is Content -> value
        else -> null
    }
