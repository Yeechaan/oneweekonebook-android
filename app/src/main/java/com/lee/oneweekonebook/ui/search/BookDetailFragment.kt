package com.lee.oneweekonebook.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.lee.oneweekonebook.databinding.FragmentBookDetailBinding

class BookDetailFragment: Fragment() {

    var binding: FragmentBookDetailBinding? = null
//    private val args: BookDetailFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
            .apply {
//                searchBook = args.book
            }

        return binding?.root
    }
}