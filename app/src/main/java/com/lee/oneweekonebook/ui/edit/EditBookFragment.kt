package com.lee.oneweekonebook.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.databinding.FragmentEditBookBinding
import com.lee.oneweekonebook.ui.edit.viewmodel.EditBookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBookFragment : Fragment() {

    private val editBookViewModel by viewModels<EditBookViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@EditBookFragment
                viewModel = editBookViewModel

                editBookViewModel.book.observe(viewLifecycleOwner, {
                    if (it.coverImage.isNotEmpty()) {
                        Glide.with(root.context).load(it.coverImage).into(imageViewBook)
                    } else {
                        Glide.with(root.context).load(R.drawable.ic_baseline_menu_book)
                            .into(imageViewBook)
                    }
                })

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