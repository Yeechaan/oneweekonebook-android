package com.lee.oneweekonebook.ui

import androidx.fragment.app.Fragment

open class NoBottomNavigationToolbarIconFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).run {
            hideToolbarIcon()
            hideBottomNavigation()
        }
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).run {
            showToolbarIcon()
            showBottomNavigation()
        }
    }

}