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
import com.lee.oneweekonebook.databinding.FragmentReadingBookDetailBinding
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModelFactory

class ReadingBookDetailFragment : Fragment() {

    private val args: ReadingBookDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookDetailViewModelFactory(bookDao, args.bookId)
        val readingBookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookDetailViewModel::class.java)

        val binding = FragmentReadingBookDetailBinding.inflate(inflater, container, false)
            .apply {

                viewModel = readingBookDetailViewModel
                lifecycleOwner = this@ReadingBookDetailFragment

                buttonDoneBook.setOnClickListener {
                    val contents = editTextTitle.text.toString()
                    val review = editTextReview.text.toString()

                    readingBookDetailViewModel.doneReadingBook(contents = contents, review = review)

//                findNavController().navigate(ReadingBookFragmentDirections.actionReadingBookFragmentToDoneBookFragment())
                }

                buttonSaveBook.setOnClickListener {
                    val contents = editTextTitle.text.toString()
                    val review = editTextReview.text.toString()

                    readingBookDetailViewModel.saveReadingBook(contents = contents, review = review)
                    Toast.makeText(requireContext(), getString(R.string.reading_save), Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }

        return binding.root
    }
}