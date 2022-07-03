package com.aprilla.thesis.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MainAppOverride: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@MainAppOverride)
            //add modules here
            modules(
                rssModule,
                mlModule,
                databaseModule,
                viewModelModule,
                repositoryModule,
                dataStoreModule
            )
        }
    }
}