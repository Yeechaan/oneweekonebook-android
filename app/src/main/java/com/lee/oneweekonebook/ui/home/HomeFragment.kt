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
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.FragmentHomeBinding
import com.lee.oneweekonebook.ui.home.model.categoryBooks
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModel
import com.lee.oneweekonebook.ui.home.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    var binding: FragmentHomeBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // PresentFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = HomeViewModelFactory(bookDao)
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
            .apply {
                viewModel = homeViewModel
                lifecycleOwner = this@HomeFragment

                buttonSearch.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchBookFragment())
                }

                buttonSuggest.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSuggestBookFragment())
                }

                val categoryBookAdapter = CategoryBookAdapter(CategoryBookListener { categoryBook ->
                    // TODO 카테고리 클릭되면 상세페이지로 이동 (TabLayout 으로 구현)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSuggestBookFragment(categoryId = categoryBook.type))

                    Toast.makeText(requireContext(), categoryBook.type.toString(), Toast.LENGTH_SHORT).show()
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