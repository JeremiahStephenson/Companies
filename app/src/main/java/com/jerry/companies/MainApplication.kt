package com.jerry.companies

import android.app.Application
import com.jerry.companies.inject.apiModule
import com.jerry.companies.inject.appModule
import com.jerry.companies.inject.cacheModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(apiModule, appModule, cacheModule)
        }
    }
}