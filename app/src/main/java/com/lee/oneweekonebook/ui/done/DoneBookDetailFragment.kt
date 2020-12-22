package com.lee.oneweekonebook.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lee.oneweekonebook.databinding.FragmentDoneBookDetailBinding

class DoneBookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentDoneBookDetailBinding.inflate(inflater, container, false).apply {

        }

        return binding.root
    }
}