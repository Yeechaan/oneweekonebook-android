package com.lee.oneweekonebook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lee.oneweekonebook.databinding.FragmentHomeBinding
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModel
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModelFactory
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookViewModel
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookViewModelFactory

class HomeFragment : Fragment() {

    // PresentFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelFactory = HomeViewModelFactory()
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val binding = FragmentHomeBinding.inflate(inflater, container, false).apply {

            viewModel = homeViewModel
            lifecycleOwner = this@HomeFragment

            buttonWish.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWishBookFragment())
            }

            buttonDone.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDoneBookFragment())
            }

            buttonSearch.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchBookFragment())
            }
        }
        return binding.root
    }
}