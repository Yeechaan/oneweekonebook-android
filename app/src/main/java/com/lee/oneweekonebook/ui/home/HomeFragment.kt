package com.lee.oneweekonebook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.databinding.FragmentHomeBinding
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModel
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    // PresentFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = HomeViewModelFactory(bookDao)
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val binding = FragmentHomeBinding.inflate(inflater, container, false).apply {

            viewModel = homeViewModel
            lifecycleOwner = this@HomeFragment

            buttonSearch.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchBookFragment())
            }

            buttonAddBook.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddBookFragment(bookType = BOOK_TYPE_READING))
            }

            buttonSuggest.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSuggestBookFragment())
            }
        }
        return binding.root
    }
}