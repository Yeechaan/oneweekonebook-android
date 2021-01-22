package com.lee.oneweekonebook.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.lee.oneweekonebook.BuildConfig.APPLICATION_ID
import com.lee.oneweekonebook.R
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var permissionResultListener: PermissionResultListener? = null

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

    fun requestPermission() {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                when {
                    // 권한 수락 클릭
                    it -> {
                        permissionResultListener?.run {
                            onGranted()
                        }
                    }
                    // 권한 거절 클릭
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                "권한이 거절되었습니다",
                                Snackbar.LENGTH_SHORT
                        ).show()
                        finishAffinity()
                    }
                    // 권한 거절 - 다시보지 않기
                    else -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:${this.packageName}")
                        startActivity(intent)
                    }
                }
            }

    fun registerPermissionResultListener(listener: PermissionResultListener) {
        permissionResultListener = listener
    }

    fun unregisterPermissionResultListener() {
        permissionResultListener = null
    }

}

interface PermissionResultListener {
    fun onGranted()
    fun onDenied(showAgain: Boolean)
}
