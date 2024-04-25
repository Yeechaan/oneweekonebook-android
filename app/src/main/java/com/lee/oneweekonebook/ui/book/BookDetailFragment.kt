package com.lee.oneweekonebook.ui.book

import android.content.Intent
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.common.ItemPickerBottomDialog
import com.lee.oneweekonebook.databinding.FragmentBookDetailBinding
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HISTORY
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.NoBottomNavigationToolbarIconFragment
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

    /**
     * 화면 이동에 대한 상태를 제어하기 위한 필드
     *
     * ViewModel에서 비지니스 로직 실행 후 observable하게 다음 화면으로 이동한 경우,
     * 기존 화면으로 돌아오려고 할 때 ViewModel의 상태가 유지되고 있어(navigation back stack) 돌아오지 못하는 경우가 있다.
     *
     * 공식문서에 따르면 Navigation logic은 UI의 관심사이기에 ViewModel이 아닌 UI에서 상태를 관리하라고 권장한다.
     * https://developer.android.com/topic/architecture/ui-layer/events#navigation-events-destination-back-stack
     * */
    private var validationInProgress: Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = bookDetailViewModel
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookDetailViewModel.uiState.collectLatest { uiState ->
                    uiState.errorMessage?.let {
                        showErrorMessage(it)
                        bookDetailViewModel.userMessageShown()
                    }

                    uiState.bookInfo?.let {
                        setBookInfo(it)
                    }

                    uiState.savedBookType?.let {
                        bookSaved(it)
                    }
                }
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setBookInfo(book: BookInfo) {
        binding.apply {
            if (book.coverImage.isNotEmpty()) {
                Glide.with(requireContext()).load(book.coverImage).into(imageViewBook)
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
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
                        bookDetailViewModel.addBook(index)
                        validationInProgress = true
                    }
                ).show(childFragmentManager, tag)
            }
        }
    }

    private fun bookSaved(bookType: Int) {
        if (validationInProgress) {
            findNavController().navigate(
                BookDetailFragmentDirections.actionBookDetailFragmentToHistoryFragment(bookType = bookType)
            )
            (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HISTORY)
            validationInProgress = false
        }
    }

}