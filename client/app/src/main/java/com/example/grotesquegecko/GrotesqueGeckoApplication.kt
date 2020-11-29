package com.example.grotesquegecko

import android.content.Context
import co.zsmb.rainbowcake.config.Loggers
import co.zsmb.rainbowcake.config.rainbowCake
import co.zsmb.rainbowcake.dagger.RainbowCakeApplication
import co.zsmb.rainbowcake.timber.TIMBER
import com.example.grotesquegecko.di.AppComponent
import com.example.grotesquegecko.di.DaggerAppComponent
import timber.log.Timber

class GrotesqueGeckoApplication : RainbowCakeApplication() {

    override lateinit var injector: AppComponent

    override fun setupInjector() {
        injector = DaggerAppComponent
            .builder()
            .context(applicationContext)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        rainbowCake {
            logger = Loggers.TIMBER
            isDebug = BuildConfig.DEBUG
        }

        Timber.plant(Timber.DebugTree())
    }
}

val Context.injector
    get() = (applicationContext as? GrotesqueGeckoApplication)?.injector
        ?: throw IllegalArgumentException("Application must extend GrotesqueGeckoApplication.")
