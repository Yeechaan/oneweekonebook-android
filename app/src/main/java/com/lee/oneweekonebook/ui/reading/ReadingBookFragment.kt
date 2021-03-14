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
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.databinding.FragmentReadingBookBinding
import com.lee.oneweekonebook.ui.history.HistoryFragmentDirections
import com.lee.oneweekonebook.ui.home.HomeFragmentDirections
import com.lee.oneweekonebook.ui.home.ReadingBookAdapter
import com.lee.oneweekonebook.ui.home.ReadingBookListener
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModelFactory

class ReadingBookFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookViewModelFactory(bookDao)
        val readingBookViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookViewModel::class.java)

        val binding = FragmentReadingBookBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@ReadingBookFragment

            val adapter = ReadingBookAdapter(ReadingBookListener { book ->
                Toast.makeText(requireContext(), book.id.toString(), Toast.LENGTH_SHORT).show()
//                findNavController().navigate(ReadingBookFragmentDirections.actionReadingBookFragmentToReadingBookDetailFragment(bookId = book.id))
                findNavController().navigate(HistoryFragmentDirections.actionHistoryReadingFragmentToReadingBookDetailFragment(bookId = book.id))
            })
            recyclerViewReadingBook.adapter = adapter

            readingBookViewModel.books.observe(viewLifecycleOwner, {
                (recyclerViewReadingBook.adapter as ReadingBookAdapter).submitList(it)
            })

            buttonAddBook.setOnClickListener {
                 findNavController().navigate(HistoryFragmentDirections.actionHistoryReadingFragmentToAddBookFragment(bookType = BOOK_TYPE_READING))
            }
        }

        return binding.root
    }
}