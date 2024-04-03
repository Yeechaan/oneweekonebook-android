package com.lee.oneweekonebook.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentSearchBookBinding
import com.lee.oneweekonebook.ui.BOTTOM_MENU_SEARCH
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookUiState
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookViewModel
import com.lee.oneweekonebook.utils.isNetworkConnected
import com.lee.oneweekonebook.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchBookFragment : Fragment() {

    private val searchBookViewModel by viewModels<SearchBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_SEARCH)

        val binding = FragmentSearchBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@SearchBookFragment
                viewModel = searchBookViewModel

                val searchBookAdapter = SearchBookAdapter(SearchBookListener { book ->
                    findNavController().navigate(
                        SearchBookFragmentDirections.actionSearchBookFragmentToBookDetailFragment(
                            isbn = book.isbn
                        )
                    )
                })
                recyclerViewSearchBook.apply {
                    adapter = searchBookAdapter
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }

                lifecycleScope.launch {
                    searchBookViewModel.uiState.collectLatest {
                        when (it) {
                            is SearchBookUiState.Success -> {
                                val books = it.books
                                searchBookAdapter.data = books

                                if (books.isEmpty()) {
                                    showMessage(getString(R.string.search_book_result_empty))
                                }
                            }

                            is SearchBookUiState.Error -> {
                                showMessage(getString(R.string.error_api_service))
                            }

                            is SearchBookUiState.Loading -> {}
                            is SearchBookUiState.Init -> {}
                        }
                    }
                }

                editTextSearchBook.setOnEditorActionListener { textView, action, _ ->
                    when (action) {
                        EditorInfo.IME_ACTION_SEARCH -> {
                            val isNetworkConnected = isNetworkConnected(requireContext())
                            val isTitleEmpty = textView.text.isNullOrEmpty()

                            when {
                                !isNetworkConnected -> showMessage(getString(R.string.error_network_not_connected))
                                isTitleEmpty -> showMessage(getString(R.string.search_book_title_empty))
                                else -> searchBookViewModel.searchBook(editTextSearchBook.text.toString())
                            }
                            true
                        }

                        else -> false
                    }
                }
            }

        return binding.root
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}