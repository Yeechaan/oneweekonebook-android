package com.lee.oneweekonebook.ui.suggest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentSuggestBookBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.NoBottomNavigationToolbarIconFragment
import com.lee.oneweekonebook.ui.home.model.categoryBook
import com.lee.oneweekonebook.ui.search.SearchBookAdapter
import com.lee.oneweekonebook.ui.search.SearchBookListener
import com.lee.oneweekonebook.ui.suggest.viewmodel.SuggestBookUiState
import com.lee.oneweekonebook.ui.suggest.viewmodel.SuggestBookViewModel
import com.lee.oneweekonebook.utils.gone
import com.lee.oneweekonebook.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SuggestBookFragment : NoBottomNavigationToolbarIconFragment() {

    private val suggestBookViewModel by viewModels<SuggestBookViewModel>()
    private val args by navArgs<SuggestBookFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (activity as MainActivity).setToolbarTitle(categoryBook[args.categoryId] ?: getString(R.string.suggest_title))

        val binding = FragmentSuggestBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@SuggestBookFragment

                val bookAdapter = SearchBookAdapter(SearchBookListener { book ->
                    findNavController().navigate(
                        SuggestBookFragmentDirections.actionSuggestBookFragmentToBookDetailFragment(
                            isbn = book.isbn
                        )
                    )
                })

                recyclerViewSuggestBook.apply {
                    adapter = bookAdapter
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }

                lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        suggestBookViewModel.uiState.collectLatest {
                            when (it) {
                                is SuggestBookUiState.Loading -> {
                                    progressCircular.visible()
                                }

                                is SuggestBookUiState.Error -> {
                                    progressCircular.gone()
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                }

                                is SuggestBookUiState.Success -> {
                                    progressCircular.gone()
                                    bookAdapter.data = it.books
                                }
                            }
                        }
                    }
                }
            }

        return binding.root
    }
}