package com.lee.oneweekonebook.ui.suggest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentSuggestBookBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.home.model.categoryBook
import com.lee.oneweekonebook.ui.search.SearchBookAdapter
import com.lee.oneweekonebook.ui.search.SearchBookListener
import com.lee.oneweekonebook.ui.suggest.viewmodel.SuggestBookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuggestBookFragment : Fragment() {

    var binding: FragmentSuggestBookBinding? = null
    private val args by navArgs<SuggestBookFragmentArgs>()

    private val suggestBookViewModel by viewModels<SuggestBookViewModel>()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTitle(
            categoryBook[args.categoryId] ?: getString(R.string.suggest_title)
        )

        binding = FragmentSuggestBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@SuggestBookFragment

                val bookAdapter = SearchBookAdapter(SearchBookListener { book ->
                    findNavController().navigate(
                        SuggestBookFragmentDirections.actionSuggestBookFragmentToBookDetailFragment(
                            book = book
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

                suggestBookViewModel.books.observe(viewLifecycleOwner, {
                    bookAdapter.data = it
                })
            }

        return binding?.root
    }

}