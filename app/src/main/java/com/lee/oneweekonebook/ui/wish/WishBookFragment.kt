package com.lee.oneweekonebook.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.databinding.FragmentWishBookBinding
import com.lee.oneweekonebook.ui.book.BookAdapter
import com.lee.oneweekonebook.ui.book.BookListener
import com.lee.oneweekonebook.ui.book.BookMoreListener
import com.lee.oneweekonebook.ui.history.HistoryFragmentDirections
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModel
import com.lee.oneweekonebook.utils.ConfirmDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishBookFragment : Fragment() {

    private var _binding: FragmentWishBookBinding? = null
    private val binding get() = _binding!!

    private val wishBookViewModel by viewModels<WishBookViewModel>()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWishBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wishBookViewModel.loadBooks()

        binding.apply {
            lifecycleOwner = this@WishBookFragment
            viewModel = wishBookViewModel

            val bookAdapter = BookAdapter(
                BookListener { book ->
                    // 책 읽기 시작
                    ConfirmDialog(
                        description = getString(R.string.dialog_book_add_description),
                        onConfirm = {
                            wishBookViewModel.addReadingBook(bookId = book.id)
                            findNavController().navigate(
                                HistoryFragmentDirections.actionHistoryWishFragmentToHistoryReadingBookFragment(
                                    bookType = BOOK_TYPE_READING
                                )
                            )
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.book_list_reading_add),
                                Toast.LENGTH_SHORT
                            ).show()
                        }).show(childFragmentManager, ConfirmDialog.TAG)
                },
                BookMoreListener { view, bookId ->
                    val popupMenu = PopupMenu(requireContext(), view)
                    setPopupBookSelection(popupMenu, bookId)
                }
            )
            recyclerViewWishBook.apply {
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
                    wishBookViewModel.uiState.collectLatest { uiState ->
                        uiState.books?.let {
                            (recyclerViewWishBook.adapter as BookAdapter).submitList(it)
                        }
                    }
                }
            }
        }
    }

    private fun setPopupBookSelection(popupMenu: PopupMenu, bookId: Int) {
        popupMenu.menuInflater.inflate(R.menu.option_type, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    findNavController().navigate(
                        HistoryFragmentDirections.actionHistoryFragmentToEditBookFragment(
                            bookId = bookId
                        )
                    )
                }

                R.id.menu_delete -> {
                    ConfirmDialog(
                        description = getString(R.string.dialog_book_delete_description),
                        onConfirm = {
                            wishBookViewModel.deleteBook(bookId = bookId)
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.book_list_delete),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ).show(childFragmentManager, ConfirmDialog.TAG)
                }
            }
            true
        }
        popupMenu.show()
    }
}