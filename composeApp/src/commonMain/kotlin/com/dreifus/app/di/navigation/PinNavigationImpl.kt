package com.dreifus.app.di.navigation

import com.dreifus.app.features.notes.navigation.PinNavigation
import com.dreifus.app.features.pin.lock.PinLockScreen
import com.dreifus.app.features.pin.setup.PinSetupScreen
import com.dreifus.navigation.controller.NavControllersHolder
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class PinNavigationImpl(
    private val navControllersHolder: NavControllersHolder,
) : PinNavigation {

    override fun openPinSetup(noteId: Long) {
        navControllersHolder.regular.navigate(PinSetupScreen(noteId))
    }

    override fun openPinLock(noteId: Long, onUnlocked: (noteId: Long) -> Unit) {
        navControllersHolder.regular.navigate(PinLockScreen(noteId, onUnlocked))
    }
}