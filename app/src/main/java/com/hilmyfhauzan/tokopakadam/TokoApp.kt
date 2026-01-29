package com.hilmyfhauzan.tokopakadam

import android.app.Application
import com.hilmyfhauzan.tokopakadam.domain.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class TokoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin error ke Logcat
            androidLogger()
            // Reference Android Context
            androidContext(this@TokoApp)
            // Load modules
            modules(appModule)
        }
    }
}