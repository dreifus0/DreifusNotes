package com.dreifus.app.di

import com.dreifus.app.data.notes.db.DatabaseDriverFactory

expect fun createDatabaseDriverFactory(): DatabaseDriverFactory
