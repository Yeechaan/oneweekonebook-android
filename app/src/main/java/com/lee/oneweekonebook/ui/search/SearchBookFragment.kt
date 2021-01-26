package com.lee.oneweekonebook.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentSearchBookBinding
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookViewModel
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookViewModelFactory

class SearchBookFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelFactory = SearchBookViewModelFactory()
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SearchBookViewModel::class.java)

        val binding = FragmentSearchBookBinding.inflate(inflater, container, false).apply {

            lifecycleOwner = viewLifecycleOwner

            buttonSearch.setOnClickListener {
                if (!editTextSearchBook.text.isNullOrEmpty()) {
                    viewModel.searchBook(editTextSearchBook.text.toString())
                } else {
                    Toast.makeText(requireContext(), getString(R.string.search_book_title_empty), Toast.LENGTH_SHORT).show()
                }
            }

            val searchBookAdapter = SearchBookAdapter()
            recyclerViewSearchBook.adapter = searchBookAdapter

            viewModel.books.observe(viewLifecycleOwner, {
                searchBookAdapter.data = it
            })
        }

        return binding.root
    }
}