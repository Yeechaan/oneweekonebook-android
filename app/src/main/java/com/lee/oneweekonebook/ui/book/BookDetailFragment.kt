package com.lee.oneweekonebook.ui.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.databinding.FragmentBookDetailBinding
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HISTORY
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.NoBottomNavigationToolbarIconFragment
import com.lee.oneweekonebook.ui.book.viewmodel.BookDetailViewModel
import com.lee.oneweekonebook.ui.book.viewmodel.BookDetailViewModelFactory
import com.lee.oneweekonebook.utils.ConfirmDialog

class BookDetailFragment : NoBottomNavigationToolbarIconFragment() {

    var binding: FragmentBookDetailBinding? = null
    private val args by navArgs<BookDetailFragmentArgs>()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = BookDetailViewModelFactory(bookDao)
        val bookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(BookDetailViewModel::class.java)

        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
            .apply {

                args.book?.let { book ->
                    searchBook = book

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
                        ConfirmDialog(
                            description = getString(R.string.dialog_book_add_description),
                            positiveMessage = getString(R.string.dialog_book_positive),
                            onConfirm = {
                                bookDetailViewModel.addBook(BOOK_TYPE_READING, book)
                                findNavController().navigate(BookDetailFragmentDirections.actionBookDetailFragmentToHistoryFragment(bookType = BOOK_TYPE_READING))
                                (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HISTORY)
                            }
                        ).show(childFragmentManager, ConfirmDialog.TAG)
                    }

                    buttonFavorite.setOnClickListener {
                        ConfirmDialog(
                            description = getString(R.string.dialog_book_wish_description),
                            positiveMessage = getString(R.string.dialog_book_positive),
                            onConfirm = {
                                bookDetailViewModel.addBook(BOOK_TYPE_WISH, book)
                                findNavController().navigate(BookDetailFragmentDirections.actionBookDetailFragmentToHistoryFragment(bookType = BOOK_TYPE_WISH))
                                (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HISTORY)
                            }
                        ).show(childFragmentManager, ConfirmDialog.TAG)
                    }
                }

            }

        return binding?.root
    }
}