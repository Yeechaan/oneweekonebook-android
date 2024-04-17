package com.lee.oneweekonebook.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentHomeBinding
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HISTORY
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HOME
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.home.model.categoryBooks
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModel
import com.lee.oneweekonebook.utils.addPreview
import com.lee.oneweekonebook.utils.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HOME)
//        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = homeViewModel
            lifecycleOwner = this@HomeFragment

            val wiseSaying = resources.getStringArray(R.array.wise_saying_list).random().split('/')
            layoutWiseSaying.textViewSayingContents.text =
                getString(R.string.home_wise_saying_content, wiseSaying[0])
            layoutWiseSaying.textViewSayingWriter.text =
                getString(R.string.home_wise_saying_writer, wiseSaying[1])


            fabAddSearch.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchBookFragment())
            }

            imageViewEmpty.setOnClickListener {
                expandableFab.performClick()
            }

            val readingBookAdapter = HomeReadingBookAdapter(HomeReadingListener { book ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToReadingBookDetailFragment(
                        bookId = book.id
                    )
                )
                (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HISTORY)
            })
            recyclerViewReadingBook.addPreview()
            recyclerViewReadingBook.adapter = readingBookAdapter
            homeViewModel.books.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    readingBookAdapter.submitList(listOf(Book(type = BOOK_TYPE_UNKNOWN)))
                } else {
                    readingBookAdapter.submitList(it)
                }
            }

            val categoryBookAdapter = CategoryBookAdapter(CategoryBookListener { categoryBook ->
                if (isNetworkConnected(requireContext())) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSuggestBookFragment(
                            categoryId = categoryBook.type
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_network_not_connected),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            val gridLayoutManager = GridLayoutManager(requireContext(), 5)
            recyclerViewCategoryBook.apply {
                layoutManager = gridLayoutManager
                adapter = categoryBookAdapter
            }
            categoryBookAdapter.data = categoryBooks
        }
    }

}