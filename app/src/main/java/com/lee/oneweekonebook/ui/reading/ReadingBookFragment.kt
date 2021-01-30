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
import com.lee.oneweekonebook.databinding.FragmentReadingBookBinding
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModelFactory

class ReadingBookFragment : Fragment() {

    private val args: ReadingBookFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookViewModelFactory(bookDao, args.bookId)
        val readingBookViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookViewModel::class.java)

        val binding = FragmentReadingBookBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel = readingBookViewModel
            lifecycleOwner = this@ReadingBookFragment

            buttonDoneBook.setOnClickListener {
                val contents = editTextTitle.text.toString()
                val review = editTextReview.text.toString()

                readingBookViewModel.doneReadingBook(contents = contents, review = review)

                findNavController().navigate(ReadingBookFragmentDirections.actionReadingBookFragmentToDoneBookFragment())
            }

            buttonSaveBook.setOnClickListener {
                val contents = editTextTitle.text.toString()
                val review = editTextReview.text.toString()

                readingBookViewModel.saveReadingBook(contents = contents, review = review)
                Toast.makeText(requireContext(), getString(R.string.reading_save), Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}