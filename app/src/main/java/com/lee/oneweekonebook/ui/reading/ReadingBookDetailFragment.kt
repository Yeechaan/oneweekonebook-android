package com.lee.oneweekonebook.ui.reading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.databinding.FragmentReadingBookDetailBinding
import com.lee.oneweekonebook.ui.BOTTOM_MENU_HOME
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModelFactory
import com.lee.oneweekonebook.utils.ConfirmDialog

class ReadingBookDetailFragment : Fragment() {

    var binding: FragmentReadingBookDetailBinding? = null
    private val args: ReadingBookDetailFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        (activity as MainActivity).setBottomNavigationStatus(BOTTOM_MENU_HOME)

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookDetailViewModelFactory(bookDao, args.bookId)
        val readingBookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookDetailViewModel::class.java)

        binding = FragmentReadingBookDetailBinding.inflate(inflater, container, false)
            .apply {
                viewModel = readingBookDetailViewModel
                lifecycleOwner = this@ReadingBookDetailFragment

                buttonDoneBook.setOnClickListener {
                    ConfirmDialog(
                        description = getString(R.string.dialog_book_done_description),
                        positiveMessage = getString(R.string.dialog_book_positive),
                        negativeMessage = getString(R.string.dialog_book_negative),
                        onConfirm = {
                            val contents = editTextTitle.text.toString()
                            val review = editTextReview.text.toString()
                            readingBookDetailViewModel.doneReadingBook(contents = contents, review = review)
                            findNavController().navigate(ReadingBookDetailFragmentDirections.actionReadingBookFragmentToHistoryBookFragment(bookType = BOOK_TYPE_DONE))
                        }
                    ).show(childFragmentManager, ConfirmDialog.TAG)
                }

                buttonSaveBook.setOnClickListener {
                    val contents = editTextTitle.text.toString()
                    val review = editTextReview.text.toString()

                    readingBookDetailViewModel.saveReadingBook(contents = contents, review = review)
                    Toast.makeText(requireContext(), getString(R.string.reading_save), Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }

                readingBookDetailViewModel.book.observe(viewLifecycleOwner, {
                    (activity as MainActivity).setToolbarTitle(it.title)
                })
            }

        return binding?.root
    }
}