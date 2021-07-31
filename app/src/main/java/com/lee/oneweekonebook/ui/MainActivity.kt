package com.lee.oneweekonebook.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.ActivityMainBinding
import com.lee.oneweekonebook.utils.gone
import com.lee.oneweekonebook.utils.visible
import dagger.hilt.android.AndroidEntryPoint

const val BOTTOM_MENU_HOME = 0
const val BOTTOM_MENU_SEARCH = 1
const val BOTTOM_MENU_HISTORY = 2

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navigation_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment,
            R.id.searchBookFragment,
            R.id.historyFragment
        ).build()

        initToolBar()
        initBottomNavigation()
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

}