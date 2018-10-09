package com.douglasharvey.fundtracker3.application

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class FundTrackerApplication : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: FundTrackerApplication
    }
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree());
        Stetho.initializeWithDefaults(this)
    }
}
