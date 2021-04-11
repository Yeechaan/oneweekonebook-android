package com.lee.oneweekonebook.ui

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lee.oneweekonebook.BuildConfig.APPLICATION_ID
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.ActivityMainKotlinBinding
import com.lee.oneweekonebook.ui.book.viewmodel.BookDetailViewModel
import com.lee.oneweekonebook.ui.book.viewmodel.BookDetailViewModelFactory
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

const val BOTTOM_MENU_HOME = 0
const val BOTTOM_MENU_SEARCH = 1
const val BOTTOM_MENU_HISTORY = 2

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainKotlinBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    private var permissionResultListener: PermissionResultListener? = null

    private var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao
        val viewModelFactory = MainViewModelFactory(bookDao)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_kotlin)
        navController = findNavController(R.id.navigation_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment,
            R.id.searchBookFragment,
            R.id.historyFragment
        ).build()
        binding.toolBarMain.setupWithNavController(navController, appBarConfiguration)
        binding.toolBarMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolBar_settings -> {
                    // TODO 관리
                    Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        initBottomNavigation()

        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(1)
            .tag(APPLICATION_ID)
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
    }

    private fun initBottomNavigation() {
        bottomNavigationView = binding.bottomNavigation

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, false)
            .build()

        val args = Bundle().apply {
//            putSerializable("dataMap", dataMap)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    navController.navigate(R.id.homeFragment, args, navOptions)
                    true
                }
                R.id.menu_search -> {
                    navController.navigate(R.id.searchBookFragment, args, navOptions)
                    true
                }
                R.id.menu_history -> {
                    navController.navigate(R.id.historyFragment, args, navOptions)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun setBottomNavigationStatus(index: Int) {
        if (::bottomNavigationView.isInitialized) {
            bottomNavigationView.menu.getItem(index).isChecked = true
        }
    }

    fun setToolbarTitle(title: String) {
        binding.toolBarMain.title = title
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
