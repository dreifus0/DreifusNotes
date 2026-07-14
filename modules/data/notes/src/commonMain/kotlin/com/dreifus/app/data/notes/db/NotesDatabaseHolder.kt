package com.dreifus.app.data.notes.db

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

/** Single shared database connection; repositories must not open their own drivers. */
@Inject
@SingleIn(AppScope::class)
class NotesDatabaseHolder(driverFactory: DatabaseDriverFactory) {
    val db: NotesDatabase = NotesDatabase(driverFactory.create())
}
