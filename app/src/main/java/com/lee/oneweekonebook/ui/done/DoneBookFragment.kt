package com.lee.oneweekonebook.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lee.oneweekonebook.databinding.FragmentDoneBookBinding
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModel
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookViewModelFactory
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModel
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModelFactory

class DoneBookFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelFactory = DoneBookViewModelFactory()
        val doneBookViewModel = ViewModelProvider(this, viewModelFactory).get(DoneBookViewModel::class.java)

        val binding = FragmentDoneBookBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel = doneBookViewModel
            lifecycleOwner = this@DoneBookFragment
        }

        return binding.root
    }
}