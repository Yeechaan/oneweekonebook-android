package com.lee.oneweekonebook.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.ActivityMainBinding
import com.lee.oneweekonebook.utils.gone
import com.lee.oneweekonebook.utils.visible
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint

const val BOTTOM_MENU_HOME = 0
const val BOTTOM_MENU_SEARCH = 1
const val BOTTOM_MENU_HISTORY = 2

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_fragment) as NavHostFragment
            navController = navHostFragment.navController
            bottomNavigation.setupWithNavController(navController)

            initBottomNavigation()

            appBarConfiguration = AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.searchBookFragment,
                R.id.historyFragment
            ).build()

            initToolBar()
        }

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
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, false)
            .build()
        val args = Bundle()

        bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setOnItemSelectedListener {
            if (bottomNavigationView.selectedItemId != it.itemId) {
                navController.navigate(it.itemId, args, navOptions)
                true
            } else false
        }
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