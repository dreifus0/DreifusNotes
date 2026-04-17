package com.dreifus.app.di

import com.dreifus.app.DreifusApplication
import com.dreifus.app.data.notes.db.DatabaseDriverFactory

actual fun createDatabaseDriverFactory(): DatabaseDriverFactory =
    DatabaseDriverFactory(DreifusApplication.instance)
