package com.lee.oneweekonebook.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lee.oneweekonebook.databinding.FragmentWishBookAddBinding

class WishBookAddFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentWishBookAddBinding.inflate(inflater, container, false).apply {

        }

        return binding.root
    }
}