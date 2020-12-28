package com.lee.oneweekonebook.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.lee.oneweekonebook.BuildConfig.APPLICATION_ID
import com.lee.oneweekonebook.R
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_kotlin)

        val navController = findNavController(R.id.navigation_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        findViewById<Toolbar>(R.id.toolBar_main)
                .setupWithNavController(navController, appBarConfiguration)

        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(1)
                .tag(APPLICATION_ID)
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

    }

}