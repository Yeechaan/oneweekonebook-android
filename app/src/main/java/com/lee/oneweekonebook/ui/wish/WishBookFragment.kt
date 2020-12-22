package com.lee.oneweekonebook.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lee.oneweekonebook.databinding.FragmentWishBookBinding
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModel

class WishBookFragment : Fragment() {

//    private val viewModel: WishBookViewModel by WishBookViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentWishBookBinding.inflate(inflater, container, false)

//        val viewModel = ViewModelProvider(this).get(WishBookViewModel::class.java)

        return binding.root
    }
}