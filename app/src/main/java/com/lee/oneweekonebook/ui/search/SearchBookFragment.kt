package com.lee.oneweekonebook.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentSearchBookBinding
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookViewModel
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookViewModelFactory

class SearchBookFragment : Fragment() {

    var binding: FragmentSearchBookBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelFactory = SearchBookViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SearchBookViewModel::class.java)

        binding = FragmentSearchBookBinding.inflate(inflater, container, false)
            .apply {

                lifecycleOwner = viewLifecycleOwner

                buttonSearch.setOnClickListener {
                    if (!editTextSearchBook.text.isNullOrEmpty()) {
                        viewModel.searchBook(editTextSearchBook.text.toString())
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.search_book_title_empty), Toast.LENGTH_SHORT).show()
                    }
                }

                val searchBookAdapter = SearchBookAdapter(SearchBookListener { book ->
                    findNavController().navigate(SearchBookFragmentDirections.actionSearchBookFragmentToBookDetailFragment(book = book))
                })
                recyclerViewSearchBook.apply {
                    adapter = searchBookAdapter
                    addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                }

                viewModel.books.observe(viewLifecycleOwner, {
                    searchBookAdapter.data = it
                })
            }

        return binding?.root
    }
}