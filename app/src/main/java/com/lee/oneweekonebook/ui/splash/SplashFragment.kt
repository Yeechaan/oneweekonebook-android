package com.lee.oneweekonebook.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lee.oneweekonebook.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    var binding: FragmentSplashBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentSplashBinding.inflate(inflater, container, false)
            .apply {

            }

        return binding?.root
    }
}