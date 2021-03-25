package com.lee.oneweekonebook.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentBookDetailBinding

class BookDetailFragment: Fragment() {

    var binding: FragmentBookDetailBinding? = null
    private val args by navArgs<BookDetailFragmentArgs>()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
            .apply {

                args.book?.let {
                    searchBook = it

                    if (it.coverImage.isNotEmpty()) {
                        Glide.with(requireContext()).load(it.coverImage).into(imageViewBook)
                    } else {
                        Glide.with(requireContext()).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
                    }
                }

            }

        return binding?.root
    }
}