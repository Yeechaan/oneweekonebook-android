package com.lee.oneweekonebook.ui.reading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.FragmentReadingBookBinding
import com.lee.oneweekonebook.ui.book.BookAdapter
import com.lee.oneweekonebook.ui.book.BookListener
import com.lee.oneweekonebook.ui.book.BookMoreListener
import com.lee.oneweekonebook.ui.history.HistoryFragmentDirections
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModelFactory

class ReadingBookFragment : Fragment() {

    var binding: FragmentReadingBookBinding? = null
    lateinit var readingBookViewModel: ReadingBookViewModel

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookViewModelFactory(bookDao)
        readingBookViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookViewModel::class.java)

        binding = FragmentReadingBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@ReadingBookFragment

                val bookAdapter = BookAdapter(
                    BookListener { book ->
                        Toast.makeText(requireContext(), book.id.toString(), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(HistoryFragmentDirections.actionHistoryReadingFragmentToReadingBookDetailFragment(bookId = book.id))
                    },
                    BookMoreListener { view, bookId ->
                        val popupMenu = PopupMenu(requireContext(), view)
                        setPopupBookSelection(popupMenu, bookId)
                    })
                recyclerViewReadingBook.apply {
                    adapter = bookAdapter
                    addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                }

                readingBookViewModel.books.observe(viewLifecycleOwner, {
                    (recyclerViewReadingBook.adapter as BookAdapter).submitList(it)
                })

            }

        return binding?.root
    }


    private fun setPopupBookSelection(popupMenu: PopupMenu, bookId: Int) {
        popupMenu.menuInflater.inflate(R.menu.option_type, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    // 수정
                    findNavController().navigate(HistoryFragmentDirections.actionHistoryFragmentToEditBookFragment(bookId = bookId))
                }
                R.id.menu_delete -> {
                    // 삭제
                    readingBookViewModel.deleteBook(bookId = bookId)
                    Toast.makeText(requireContext(), getString(R.string.book_list_delete), Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
}