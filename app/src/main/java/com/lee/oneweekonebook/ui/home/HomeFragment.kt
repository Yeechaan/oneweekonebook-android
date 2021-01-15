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

            buttonWish.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWishBookFragment())
            }

            buttonDone.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDoneBookFragment())
            }

            buttonSearch.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchBookFragment())
            }

            buttonAddBook.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddBookFragment())
            }

            val adapter = ReadingBookAdapter(ReadingBookListener { book ->
                Toast.makeText(requireContext(), book.id.toString(), Toast.LENGTH_SHORT).show()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToReadingBookFragment(bookId = book.id))
            })
            recyclerViewReadingBook.adapter = adapter

            homeViewModel.books.observe(viewLifecycleOwner, {
                (recyclerViewReadingBook.adapter as ReadingBookAdapter).submitList(it)
            })
        }
        return binding.root
    }
}