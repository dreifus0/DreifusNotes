package com.dreifus.navigation.controller

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import com.dreifus.navigation.screen.BaseScreen

@Stable
class NavController<T : BaseScreen>(
    vararg screens: T,
    /** Prevents navigation to a screen with the same class as the current one. Relevant for overlays. */
    private val filterNavigationToSameClass: Boolean = false,
    private val isEmptyBackstackPossible: Boolean = true,
) {
    val backstack = mutableStateListOf(*screens)

    fun navigate(screen: T) {
        if (!filterNavigationToSameClass || backstack.lastOrNull()
                ?.let { it::class } != screen::class
        ) {
            backstack.add(screen)
        } else {
            println("NavController: Attempted to navigate to the same screen class as the current one: ${screen::class}")
        }
    }

    fun replaceLast(screen: T) {
        if (backstack.isNotEmpty()) {
            backstack[backstack.size - 1] = screen
        } else {
            if (!isEmptyBackstackPossible) {
                println("NavController.replaceLast: Invoke on empty backstack")
            }
        }
    }

    fun replaceAll(screens: List<T>) {
        if (screens.isEmpty() && !isEmptyBackstackPossible) {
            println("NavController.replaceAll: destinations list is empty")
        }
        backstack.clear()
        backstack.addAll(screens)
    }

    fun replaceAll(screen: T) {
        replaceAll(listOf(screen))
    }

    fun pop() {
        when {
            !isEmptyBackstackPossible && backstack.isEmpty() ->
                println("NavController.pop: Invoke on empty backstack")

            !isEmptyBackstackPossible && backstack.size == 1 ->
                println("NavController.pop: Can't pop last item")

            else -> backstack.removeLastOrNull()
        }
    }

    fun popAll() {
        if (backstack.isNotEmpty()) {
            backstack.removeRange(1, backstack.size)
        } else {
            if (!isEmptyBackstackPossible) {
                println("NavController.popAll: Invoke on empty backstack")
            }
        }
    }

    /**
     * Pop up to the first screen that matches the [predicate].
     * If [inclusive] is true, the matching screen will also be popped.
     * If [toFirst] is true, the backstack will be cleared to the first screen in the backstack.
     */
    fun popUpTo(
        inclusive: Boolean = false,
        toFirst: Boolean = false,
        predicate: (T) -> Boolean,
    ) {
        val index =
            if (toFirst) backstack.indexOfFirst(predicate) else backstack.indexOfLast(predicate)
        when {
            backstack.isEmpty() -> println("NavController.popUpTo: Invoke on empty backstack")
            index == -1 -> println("NavController.popUpTo: Destination doesn't exist")
            !isEmptyBackstackPossible && backstack.size == 1 ->
                println("NavController.popUpTo: Can't pop last item")

            else -> {
                val correctedIndex = if (inclusive) index else index + 1
                backstack.removeRange(correctedIndex, backstack.size)
            }
        }
    }

    /**
     * Pops all screens off the backstack up to the screen that meets the condition
     * of the [predicate] and replaces them with [newScreens].
     *
     * The order of the items in the list is interpreted as going from the bottom of the backstack
     * to the top. It means that the last item of the list will become the currently displayed item.
     *
     * The [newScreens] list may be empty.
     *
     * @param [inclusive] whether the item itself should be popped or not, default value is `false`
     *
     * @param [toFirst] specifies the policy of selecting the target item in case of multiple matching
     * items. By default, the last matching item from the start of the backstack will be considered
     * the point up to which to replace.
     */
    fun replaceUpTo(
        newScreens: List<T>,
        inclusive: Boolean = false,
        toFirst: Boolean = false,
        predicate: (T) -> Boolean,
    ) {
        val index =
            if (toFirst) backstack.indexOfFirst(predicate) else backstack.indexOfLast(predicate)
        when {
            index == -1 -> println("NavController.replaceUpTo: Destination doesn't exist")
            else -> {
                val correctedIndex = if (inclusive) index else index + 1
                backstack.removeRange(correctedIndex, backstack.size)
                backstack.addAll(newScreens)
            }
        }
    }

    /**
     * Pops all screens off the backstack up to the screen that meets the condition
     * of the [predicate] and replaces them with a [newScreen].
     *
     * @param [inclusive] whether the item itself should be popped or not, default value is `false`
     *
     * @param [toFirst] specifies the policy of selecting the target item in case of multiple matching
     * items. By default, the last matching item from the start of the backstack will be considered
     * the point up to which to replace.
     */
    fun replaceUpTo(
        newScreen: T,
        inclusive: Boolean = false,
        toFirst: Boolean = false,
        predicate: (T) -> Boolean,
    ) = replaceUpTo(listOf(newScreen), inclusive, toFirst, predicate)
}
