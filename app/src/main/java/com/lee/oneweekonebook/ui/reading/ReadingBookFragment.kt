package com.lee.oneweekonebook.ui.reading

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
import com.lee.oneweekonebook.databinding.FragmentReadingBookBinding
import com.lee.oneweekonebook.ui.book.BookAdapter
import com.lee.oneweekonebook.ui.book.BookListener
import com.lee.oneweekonebook.ui.book.BookMoreListener
import com.lee.oneweekonebook.ui.history.HistoryFragmentDirections
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModel
import com.lee.oneweekonebook.utils.ConfirmDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingBookFragment : Fragment() {

    private val readingBookViewModel by viewModels<ReadingBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentReadingBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@ReadingBookFragment
                viewModel = readingBookViewModel

                val bookAdapter = BookAdapter(
                    BookListener { book ->
                        findNavController().navigate(
                            HistoryFragmentDirections.actionHistoryReadingFragmentToReadingBookDetailFragment(
                                bookId = book.id
                            )
                        )
                    },
                    BookMoreListener { view, bookId ->
                        val popupMenu = PopupMenu(requireContext(), view)
                        setPopupBookSelection(popupMenu, bookId)
                    })
                recyclerViewReadingBook.apply {
                    adapter = bookAdapter
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }

                readingBookViewModel.books.observe(viewLifecycleOwner) {
                    (recyclerViewReadingBook.adapter as BookAdapter).submitList(it)
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
                            readingBookViewModel.deleteBook(bookId = bookId)
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