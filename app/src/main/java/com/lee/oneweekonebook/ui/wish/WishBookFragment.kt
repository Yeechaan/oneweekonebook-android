package com.lee.oneweekonebook.ui.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class WishBookFragment : Fragment() {

    private val wishBookViewModel by viewModels<WishBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWishBookBinding.inflate(inflater, container, false).apply {
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

            wishBookViewModel.books.observe(viewLifecycleOwner) {
                (recyclerViewWishBook.adapter as BookAdapter).submitList(it)
            }

        }

        return binding.root
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