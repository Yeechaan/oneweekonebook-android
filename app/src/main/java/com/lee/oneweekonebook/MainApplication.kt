package com.lee.oneweekonebook

import android.app.Application
import com.lee.oneweekonebook.utils.CustomDiskLogAdapter
import com.lee.oneweekonebook.utils.LOGGER_DIRECTORY_NAME
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp
import java.io.File

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

        val diskPath = applicationContext.filesDir.path
        val folder = "$diskPath/$LOGGER_DIRECTORY_NAME"
        val file = File(folder)

        Logger.addLogAdapter(CustomDiskLogAdapter(file))
    }

}