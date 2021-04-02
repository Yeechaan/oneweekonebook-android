package com.lee.oneweekonebook.ui.reading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.FragmentReadingBookBinding
import com.lee.oneweekonebook.ui.history.HistoryFragmentDirections
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookViewModelFactory

class ReadingBookFragment : Fragment() {

    var binding: FragmentReadingBookBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookViewModelFactory(bookDao)
        val readingBookViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookViewModel::class.java)

        binding = FragmentReadingBookBinding.inflate(inflater, container, false)
            .apply {
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

            }

        return binding?.root
    }
}