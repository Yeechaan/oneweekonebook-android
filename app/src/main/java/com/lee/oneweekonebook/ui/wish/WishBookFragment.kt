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
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentWishBookBinding
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModel
import com.lee.oneweekonebook.ui.wish.viewmodel.WishBookViewModelFactory

class WishBookFragment : Fragment() {

    private lateinit var viewModel: WishBookViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = WishBookViewModelFactory(bookDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WishBookViewModel::class.java)

        val binding = FragmentWishBookBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@WishBookFragment

            buttonAddBook.setOnClickListener {
                findNavController().navigate(WishBookFragmentDirections.actionWishBookFragmentToAddBookFragment(bookType = BOOK_TYPE_WISH))
            }

            val adapter = WishBookAdapter(WishBookListener { book, view ->
                val popupMenu = PopupMenu(requireContext(), view)
                setPopupBookSelection(popupMenu, book)
            })
            recyclerViewWishBook.adapter = adapter

            viewModel.books.observe(viewLifecycleOwner, {
                (recyclerViewWishBook.adapter as WishBookAdapter).submitList(it)
            })

        }

        return binding.root
    }

    private fun setPopupBookSelection(popupMenu: PopupMenu, book: Book) {
        popupMenu.menuInflater.inflate(R.menu.option_type, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.m1 -> {
                    // 책 읽기 시작
                    viewModel.addReadingBook(bookId = book.id)
                    findNavController().navigateUp()
                    Toast.makeText(requireContext(), getString(R.string.book_list_reading_add), Toast.LENGTH_SHORT).show()
                }
                R.id.m2 -> {
                    // 수정
                    findNavController().navigate(WishBookFragmentDirections.actionWishBookFragmentToAddBookFragment(bookType = book.type, bookId = book.id.toString()))
                }
                R.id.m3 -> {
                    // 삭제
                    viewModel.deleteBook(bookId = book.id)
                    Toast.makeText(requireContext(), getString(R.string.book_list_delete), Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
}