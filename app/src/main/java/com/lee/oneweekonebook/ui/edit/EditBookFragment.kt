package com.lee.oneweekonebook.ui.edit

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
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentEditBookBinding
import com.lee.oneweekonebook.ui.edit.viewmodel.EditBookViewModel
import com.lee.oneweekonebook.ui.edit.viewmodel.EditBookViewModelFactory

class EditBookFragment : Fragment() {

    private val args: EditBookFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = EditBookViewModelFactory(bookDao, args.bookId)
        val editBookViewModel = ViewModelProvider(this, viewModelFactory).get(EditBookViewModel::class.java)

        val binding = FragmentEditBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@EditBookFragment
                viewModel = editBookViewModel

                if (editBookViewModel.book.coverImage.isNotEmpty()) {
                    Glide.with(root.context).load(editBookViewModel.book.coverImage).into(imageViewBook)
                } else {
                    Glide.with(root.context).load(R.drawable.ic_baseline_menu_book).into(imageViewBook)
                }

                buttonDone.setOnClickListener {
                    val editedBook = Book(
                        title = editTextTitle.text.toString(),
                        writer = editTextWriter.text.toString(),
                        publisher = editTextPublisher.text.toString(),
                    )

                    editBookViewModel.editBook(editedBook)

                    findNavController().navigateUp()
                }
            }

        return binding.root
    }
}