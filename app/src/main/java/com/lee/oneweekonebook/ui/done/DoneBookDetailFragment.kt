package com.lee.oneweekonebook.ui.done

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
import com.lee.oneweekonebook.databinding.FragmentDoneBookDetailBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookDetailViewModel
import com.lee.oneweekonebook.ui.done.viewmodel.DoneBookDetailViewModelFactory

class DoneBookDetailFragment : Fragment() {

    var binding: FragmentDoneBookDetailBinding? = null
    private val args by navArgs<DoneBookDetailFragmentArgs>()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = DoneBookDetailViewModelFactory(bookDao, args.bookId)
        val doneBookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(DoneBookDetailViewModel::class.java)

        binding = FragmentDoneBookDetailBinding.inflate(inflater, container, false)
            .apply {
                viewModel = doneBookDetailViewModel
                lifecycleOwner = this@DoneBookDetailFragment

                buttonSaveBook.setOnClickListener {
                    val contents = editTextContents.text.toString()
                    val review = editTextReview.text.toString()

                    doneBookDetailViewModel.saveReadingBook(contents = contents, review = review)
                    Toast.makeText(requireContext(), getString(R.string.reading_save), Toast.LENGTH_SHORT).show()
                }

                buttonContents.setOnClickListener {
                    doneBookDetailViewModel.setCurrentPage(isContentsPage = true)
                }

                buttonReview.setOnClickListener {
                    doneBookDetailViewModel.setCurrentPage(isContentsPage = false)
                }

                doneBookDetailViewModel.book.observe(viewLifecycleOwner, {
                    (activity as MainActivity).setToolbarTitle(it.title)

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