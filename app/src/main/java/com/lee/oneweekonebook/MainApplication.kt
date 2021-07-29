package com.lee.oneweekonebook

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(1)
            .tag(BuildConfig.APPLICATION_ID)
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

}