package com.lee.oneweekonebook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentHomeBinding
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HISTORY
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HOME
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.home.model.categoryBooks
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModel
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModelFactory
import com.lee.oneweekonebook.utils.isNetworkConnected
import com.orhanobut.logger.Logger

const val PREVIOUS_ADD = 1

class HomeFragment : Fragment() {

    var binding: FragmentHomeBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HOME)

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = HomeViewModelFactory(bookDao)
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
            .apply {
                viewModel = homeViewModel
                lifecycleOwner = this@HomeFragment

//                buttonSuggest.setOnClickListener {
//                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSuggestBookFragment())
//                }

                val wiseSaying = resources.getStringArray(R.array.wise_saying_list).random().split('/')
                layoutWiseSaying.textViewSayingContents.text = getString(R.string.home_wise_saying_content, wiseSaying[0])
                layoutWiseSaying.textViewSayingWriter.text = getString(R.string.home_wise_saying_writer, wiseSaying[1])


                fabAddSearch.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchBookFragment(previous = PREVIOUS_ADD))
                }

                fabAddCamera.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddBookFragment())
                }

                imageViewEmpty.setOnClickListener {
                    expandableFab.performClick()
                }

                val readingBookAdapter = HomeReadingAdapter(HomeReadingListener { book ->
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToReadingBookDetailFragment(bookId = book.id))
                    (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HISTORY)
                })
                recyclerViewReadingBook.apply {
                    adapter = readingBookAdapter
                }
                homeViewModel.books.observe(viewLifecycleOwner, {
                    if (it.isEmpty()) {
                        readingBookAdapter.data = listOf(Book(type = BOOK_TYPE_UNKNOWN))
                    } else {
                        readingBookAdapter.data = it
                    }
                })

                val categoryBookAdapter = CategoryBookAdapter(CategoryBookListener { categoryBook ->
                    // TODO 카테고리 클릭되면 상세페이지로 이동 (TabLayout 으로 구현)
                    if (isNetworkConnected(requireContext())) {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSuggestBookFragment(categoryId = categoryBook.type))
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.error_network_not_connected), Toast.LENGTH_SHORT).show()
                    }
                })
                val gridLayoutManager = GridLayoutManager(requireContext(), 5)
                recyclerViewCategoryBook.apply {
                    layoutManager = gridLayoutManager
                    adapter = categoryBookAdapter
                }
                categoryBookAdapter.data = categoryBooks

            }
        return binding?.root
    }

}