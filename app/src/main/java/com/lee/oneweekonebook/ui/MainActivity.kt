package com.lee.oneweekonebook.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private var permissionResultListener: PermissionResultListener? = null

    private var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false

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

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
    }


    fun registerPermissionResultListener(listener: PermissionResultListener) {
        permissionResultListener = listener
    }

    fun unregisterPermissionResultListener() {
        permissionResultListener = null
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissions.map {
            Logger.d(it)
        }

        if (requestCode == 1) {
            Logger.d("onRequestPermissionsResult")

            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Logger.d("PERMISSION_GRANTED")

                    allgranted = true
                } else {
                    Logger.d("PERMISSION_DENIED")

                    allgranted = false
                    break
                }
            }

            if (allgranted) {
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
                permissionResultListener?.onGranted()

            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])) {

                getAlertDialog()
            } else {
                Toast.makeText(applicationContext, "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Multiple Permissions")
        builder.setMessage("This app needs permissions.")
        builder.setPositiveButton("Grant") { dialog, which ->
            dialog.cancel()
            ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    override fun onPostResume() {
        super.onPostResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun requirePermission() {
        val needPermissions = arrayListOf<String>()

        permissionsRequired.map {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED) {
                needPermissions.add(it)
            }
        }
        // android 10 에서는 external storage permission 동작 안함
        // android 8 에서는 external storage permission 동작
        if (needPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, needPermissions.toTypedArray(), 1)
        } else {
            permissionResultListener?.onGranted()
            Logger.d("requirePermission all")
        }
    }

}

interface PermissionResultListener {
    fun onGranted()
    fun onDenied(showAgain: Boolean)
}
