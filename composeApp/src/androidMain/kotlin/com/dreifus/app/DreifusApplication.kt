package com.dreifus.app

import android.app.Application

class DreifusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        // Use only as a last resort when no other DI or context injection is possible.
        lateinit var instance: Application
            private set
    }
}
