package com.lee.oneweekonebook.ui.reading

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.databinding.FragmentReadingBookDetailBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.NoBottomNavigationToolbarIconFragment
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModelFactory
import com.lee.oneweekonebook.utils.ConfirmDialog
import com.lee.oneweekonebook.utils.gone

class ReadingBookDetailFragment : NoBottomNavigationToolbarIconFragment() {

    var binding: FragmentReadingBookDetailBinding? = null
    private val args: ReadingBookDetailFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookDetailViewModelFactory(bookDao, args.bookId)
        val readingBookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookDetailViewModel::class.java)

        binding = FragmentReadingBookDetailBinding.inflate(inflater, container, false)
            .apply {
                viewModel = readingBookDetailViewModel
                lifecycleOwner = this@ReadingBookDetailFragment

                buttonContents.setOnClickListener {
                    readingBookDetailViewModel.setCurrentPage(isContentsPage = true)
                }

                buttonReview.setOnClickListener {
                    readingBookDetailViewModel.setCurrentPage(isContentsPage = false)
                }

                buttonDoneBook.setOnClickListener {
                    ConfirmDialog(
                        description = getString(R.string.dialog_book_done_description),
                        positiveMessage = getString(R.string.dialog_book_positive),
                        onConfirm = {
                            val contents = editTextContents.text.toString()
                            val review = editTextReview.text.toString()

                            readingBookDetailViewModel.saveBook(type = BOOK_TYPE_DONE, contents = contents, review = review)
                            findNavController().navigate(ReadingBookDetailFragmentDirections.actionReadingBookFragmentToHistoryBookFragment(bookType = BOOK_TYPE_DONE))
                        }
                    ).show(childFragmentManager, ConfirmDialog.TAG)
                }

                buttonSaveBook.setOnClickListener {
                    val contents = editTextContents.text.toString()
                    val review = editTextReview.text.toString()

                    readingBookDetailViewModel.saveBook(type = BOOK_TYPE_READING, contents = contents, review = review)
                    Toast.makeText(requireContext(), getString(R.string.reading_save), Toast.LENGTH_SHORT).show()
                }

                readingBookDetailViewModel.book.observe(viewLifecycleOwner, {
                    (activity as MainActivity).setToolbarTitle(it.title)

                    layoutBook.imageViewTo.gone()
                    layoutBook.textViewTo.gone()
                    if (it.coverImage.isNotEmpty()) {
                        Glide.with(root.context).load(it.coverImage).into(layoutBook.imageViewBook)
                    } else {
                        Glide.with(root.context).load(R.drawable.ic_baseline_menu_book).into(layoutBook.imageViewBook)
                    }
                })
            }

        return binding?.root
    }

}