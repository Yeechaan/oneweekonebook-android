package com.lee.oneweekonebook.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentBookContentsBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.utils.toEditable

class BookContentsFragment : Fragment() {

    private var binding: FragmentBookContentsBinding? = null
    lateinit var book: Book

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBookContentsBinding.inflate(inflater, container, false)
            .apply {

                book = (activity as MainActivity).mainViewModel.getCurrentBook()
                editTextContents.text = book.contents.toEditable()

                textViewBookSave.setOnClickListener {
                    book.contents = editTextContents.text.toString()

                    (activity as MainActivity).mainViewModel.saveBook(book)
                    Toast.makeText(requireContext(), getString(R.string.reading_save), Toast.LENGTH_SHORT).show()
                }
            }

        return binding?.root
    }

    override fun onStop() {
        super.onStop()

        book.contents = binding?.editTextContents?.text.toString()
        (activity as MainActivity).mainViewModel.saveBook(book)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}