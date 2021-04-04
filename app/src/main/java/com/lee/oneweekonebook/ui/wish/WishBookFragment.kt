package com.lee.oneweekonebook.ui.wish

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
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentWishBookBinding
import com.lee.oneweekonebook.ui.book.BookAdapter
import com.lee.oneweekonebook.ui.book.BookListener
import com.lee.oneweekonebook.ui.book.BookMoreListener
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModel
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModelFactory

class WishBookFragment : Fragment() {

    var binding: FragmentWishBookBinding? = null
    private lateinit var viewModel: WishBookViewModel

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = WishBookViewModelFactory(bookDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WishBookViewModel::class.java)

        binding = FragmentWishBookBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@WishBookFragment

            val bookAdapter = BookAdapter(
                BookListener { book ->
//                    val popupMenu = PopupMenu(requireContext(), view)
//                    setPopupBookSelection(popupMenu, book)
                },
                BookMoreListener { view, bookId ->
                    Toast.makeText(requireContext(), "book.id.toString()", Toast.LENGTH_SHORT).show()
                    val popupMenu = PopupMenu(requireContext(), view)
                    setPopupBookSelection(popupMenu, bookId)
                }
            )
            recyclerViewWishBook.apply {
                adapter = bookAdapter
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }

            viewModel.books.observe(viewLifecycleOwner, {
                (recyclerViewWishBook.adapter as BookAdapter).submitList(it)
            })

        }

        return binding?.root
    }

    private fun setPopupBookSelection(popupMenu: PopupMenu, bookId: Int) {
        popupMenu.menuInflater.inflate(R.menu.option_type, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.m1 -> {
                    // 책 읽기 시작
                    viewModel.addReadingBook(bookId = bookId)
                    findNavController().navigateUp()
                    Toast.makeText(requireContext(), getString(R.string.book_list_reading_add), Toast.LENGTH_SHORT).show()
                }
                R.id.m2 -> {
                    // 수정
//                    findNavController().navigate(HistoryFragmentDirections.actionHistoryWishFragmentToAddBookFragment(bookType = BOOK_TYPE_WISH, bookId = book.id.toString()))
                }
                R.id.m3 -> {
                    // 삭제
                    viewModel.deleteBook(bookId = bookId)
                    Toast.makeText(requireContext(), getString(R.string.book_list_delete), Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
}