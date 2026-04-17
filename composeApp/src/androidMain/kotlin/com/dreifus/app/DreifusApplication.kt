package com.dreifus.app

import android.app.Application
import android.content.Context

class DreifusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: Context
            private set
    }
}
