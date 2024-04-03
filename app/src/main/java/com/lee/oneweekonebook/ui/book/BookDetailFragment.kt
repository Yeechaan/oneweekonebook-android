package com.lee.oneweekonebook.ui.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.common.ItemPickerBottomDialog
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.databinding.FragmentBookDetailBinding
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HISTORY
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.NoBottomNavigationToolbarIconFragment
import com.lee.oneweekonebook.ui.book.viewmodel.BookDetailUiState
import com.lee.oneweekonebook.ui.book.viewmodel.BookDetailViewModel
import com.lee.oneweekonebook.ui.search.model.BookInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookDetailFragment : NoBottomNavigationToolbarIconFragment() {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding: FragmentBookDetailBinding
        get() = _binding!!

    private val bookDetailViewModel by viewModels<BookDetailViewModel>()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
//        bookDetailViewModel.clearState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            bookDetailViewModel.uiState.collectLatest {
                when (it) {
                    is BookDetailUiState.Success -> {
                        bookSuccess(it.bookInfo)
                    }

                    is BookDetailUiState.Saved -> {
                        bookSaved(it.bookType)
                    }

                    is BookDetailUiState.Error -> {
                        Toast.makeText(requireContext(), getString(R.string.error_api_service), Toast.LENGTH_SHORT).show()
                    }

                    is BookDetailUiState.Loading -> {}
                }
            }
        }

        return binding.root
    }

    private fun bookSuccess(book: BookInfo) {
        binding.apply {
            searchBook = book

            if (book.coverImage.isNotEmpty()) {
                Glide.with(requireContext()).load(book.coverImage).into(imageViewBook)
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_baseline_menu_book)
                    .into(imageViewBook)
            }

            imageViewLink.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(book.link))
                startActivity(browserIntent)
            }

            buttonAddBook.setOnClickListener {
                ItemPickerBottomDialog(
                    title = getString(R.string.add_book),
                    items = listOf(
                        getString(R.string.wish_title),
                        getString(R.string.add_book_reading),
                        getString(R.string.add_book_done)
                    ),
                    onPick = { index, _ ->
                        bookDetailViewModel.addBook(index, book)
                    },
                    onDismiss = {

                    }
                ).show(childFragmentManager, tag)
            }
        }
    }

    private fun bookSaved(bookType: Int) {
        when (bookType) {
            BOOK_TYPE_UNKNOWN -> Toast.makeText(
                requireContext(),
                "독서내역에 추가된 책 입니다!",
                Toast.LENGTH_SHORT
            ).show()

            BOOK_TYPE_WISH -> {
                findNavController().navigate(
                    BookDetailFragmentDirections.actionBookDetailFragmentToHistoryFragment(
                        bookType = BOOK_TYPE_WISH
                    )
                )
                (activity as MainActivity).setBottomNavigationStatus(
                    BOTTOM_MENU_HISTORY
                )
            }

            BOOK_TYPE_READING -> {
                findNavController().navigate(
                    BookDetailFragmentDirections.actionBookDetailFragmentToHistoryFragment(
                        bookType = BOOK_TYPE_READING
                    )
                )
                (activity as MainActivity).setBottomNavigationStatus(
                    BOTTOM_MENU_HISTORY
                )
            }

            BOOK_TYPE_DONE -> {
                findNavController().navigate(
                    BookDetailFragmentDirections.actionBookDetailFragmentToHistoryFragment(
                        bookType = BOOK_TYPE_DONE
                    )
                )
                (activity as MainActivity).setBottomNavigationStatus(
                    BOTTOM_MENU_HISTORY
                )
            }
        }
    }

}