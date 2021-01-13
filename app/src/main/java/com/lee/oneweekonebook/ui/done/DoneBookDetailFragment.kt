package com.lee.oneweekonebook.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lee.oneweekonebook.databinding.FragmentDoneBookDetailBinding
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookDetailViewModel
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookDetailViewModelFactory
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModel
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModelFactory

class DoneBookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelFactory = DoneBookDetailViewModelFactory()
        val doneBookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(DoneBookDetailViewModel::class.java)

        val binding = FragmentDoneBookDetailBinding.inflate(inflater, container, false).apply {
            viewModel = doneBookDetailViewModel

        }

        return binding.root
    }
}