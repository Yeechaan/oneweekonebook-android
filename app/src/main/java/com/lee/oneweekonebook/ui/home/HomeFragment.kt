package com.lee.oneweekonebook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lee.oneweekonebook.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // PresentFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false).apply {

            buttonWish.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWishBookFragment())
            }

            buttonDone.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDoneBookFragment())
            }

        }
        return binding.root
    }
}