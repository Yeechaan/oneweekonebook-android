package com.lee.oneweekonebook.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.databinding.FragmentDoneBookDetailBinding
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

                doneBookDetailViewModel.book.observe(viewLifecycleOwner, {
                    imageViewCover
                    if (it.coverImage.isNotEmpty()) {
                        Glide.with(requireContext()).load(it.coverImage).into(imageViewCover)
                    } else {
                        Glide.with(requireContext()).load(R.drawable.ic_baseline_menu_book).into(imageViewCover)
                    }
                })

                textViewEditDone.setOnClickListener {
                    val contents = editTextContents.text.toString()
                    val review = editTextReview.text.toString()

                    doneBookDetailViewModel.saveReadingBook(contents = contents, review = review)
                }
            }

        return binding?.root
    }

}