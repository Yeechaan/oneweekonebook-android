package com.lee.oneweekonebook.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lee.oneweekonebook.BuildConfig.APPLICATION_ID
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.ActivityMainKotlinBinding
import com.lee.oneweekonebook.utils.gone
import com.lee.oneweekonebook.utils.visible
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
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var permissionResultListener: PermissionResultListener? = null

    private var permissionsRequired =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao
        val viewModelFactory = MainViewModelFactory(bookDao)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_kotlin)
        navController = findNavController(R.id.navigation_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment,
            R.id.searchBookFragment,
            R.id.historyFragment
        ).build()

        initToolBar()
        initBottomNavigation()
        initLogger()
    }

    private fun initToolBar() {
        binding.toolBarMain.setupWithNavController(navController, appBarConfiguration)
        binding.toolBarMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolBar_settings -> navController.navigate(R.id.navigation_setting)
            }
            true
        }
    }

    private fun initBottomNavigation() {
        bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setOnNavigationItemSelectedListener {
            if (bottomNavigationView.selectedItemId != it.itemId) {
                val moveTo = when (it.itemId) {
                    R.id.menu_home -> BOTTOM_MENU_HOME
                    R.id.menu_search -> BOTTOM_MENU_SEARCH
                    R.id.menu_history -> BOTTOM_MENU_HISTORY
                    else -> BOTTOM_MENU_HOME
                }
                navigate(moveTo)
                true
            } else false
        }
    }

    private fun navigate(moveTo: Int = BOTTOM_MENU_HOME) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, false)
            .build()
        val args = Bundle()

        val destination = when (moveTo) {
            BOTTOM_MENU_HOME -> R.id.homeFragment
            BOTTOM_MENU_SEARCH -> R.id.searchBookFragment
            BOTTOM_MENU_HISTORY -> R.id.historyFragment
            else -> R.id.homeFragment
        }

        navController.navigate(destination, args, navOptions)
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(1)
            .tag(APPLICATION_ID)
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentDestination = navController.currentDestination

        when (currentDestination?.id) {
            R.id.homeFragment -> finish()
            else -> super.onBackPressed()
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

    fun showToolbarIcon() {
        binding.toolBarMain.menu[0].isVisible = true
    }

    fun hideToolbarIcon() {
        binding.toolBarMain.menu[0].isVisible = false
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.visible()
    }

    fun hideBottomNavigation() {
        binding.bottomNavigation.gone()
    }

    fun registerPermissionResultListener(listener: PermissionResultListener) {
        permissionResultListener = listener
    }

    fun unregisterPermissionResultListener() {
        permissionResultListener = null
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            //check if all permissions are granted
            var permissionAllGranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissionAllGranted = true
                } else {
                    permissionAllGranted = false
                    break
                }
            }

            if (permissionAllGranted) {
                permissionResultListener?.onGranted()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])) {
                permissionResultListener?.onDenied(false)
            } else {
                Logger.d("permission all denied")
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
        }
    }

}

interface PermissionResultListener {
    fun onGranted()
    fun onDenied(showAgain: Boolean)
}
