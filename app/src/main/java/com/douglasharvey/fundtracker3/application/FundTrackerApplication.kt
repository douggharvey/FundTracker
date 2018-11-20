package com.douglasharvey.fundtracker3.application

import android.app.Application
import com.douglasharvey.fundtracker3.utilities.Prefs
import com.facebook.stetho.Stetho
import timber.log.Timber

// https://blog.teamtreehouse.com/making-sharedpreferences-easy-with-kotlin

val prefs: Prefs by lazy {
    FundTrackerApplication.prefs!!
}

class FundTrackerApplication : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: FundTrackerApplication
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
        Timber.plant(Timber.DebugTree());
        Stetho.initializeWithDefaults(this)
    }
}
