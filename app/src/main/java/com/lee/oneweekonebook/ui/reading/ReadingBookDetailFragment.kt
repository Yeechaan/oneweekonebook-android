package com.lee.oneweekonebook.ui.reading

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.databinding.FragmentReadingBookDetailBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.NoBottomNavigationToolbarIconFragment
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModel
import com.lee.oneweekonebook.utils.ConfirmDialog
import com.lee.oneweekonebook.utils.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingBookDetailFragment : NoBottomNavigationToolbarIconFragment() {

    private val readingBookDetailViewModel by viewModels<ReadingBookDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentReadingBookDetailBinding.inflate(inflater, container, false)
            .apply {
                viewModel = readingBookDetailViewModel
                lifecycleOwner = this@ReadingBookDetailFragment

                buttonContents.setOnClickListener {
                    readingBookDetailViewModel.setCurrentPage(isContentsPage = true)
                }

                buttonReview.setOnClickListener {
                    readingBookDetailViewModel.setCurrentPage(isContentsPage = false)
                }

                buttonDoneBook.setOnClickListener {
                    ConfirmDialog(
                        description = getString(R.string.dialog_book_done_description),
                        positiveMessage = getString(R.string.dialog_book_positive),
                        onConfirm = {
                            val contents = editTextContents.text.toString()
                            val review = editTextReview.text.toString()

                            readingBookDetailViewModel.saveBook(
                                type = BOOK_TYPE_DONE,
                                contents = contents,
                                review = review
                            )
                            findNavController().navigate(
                                ReadingBookDetailFragmentDirections.actionReadingBookFragmentToHistoryBookFragment(
                                    bookType = BOOK_TYPE_DONE
                                )
                            )
                        }
                    ).show(childFragmentManager, ConfirmDialog.TAG)
                }

                buttonSaveBook.setOnClickListener {
                    val contents = editTextContents.text.toString()
                    val review = editTextReview.text.toString()

                    readingBookDetailViewModel.saveBook(
                        type = BOOK_TYPE_READING,
                        contents = contents,
                        review = review
                    )
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.reading_save),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                readingBookDetailViewModel.book.observe(viewLifecycleOwner, {
                    (activity as MainActivity).setToolbarTitle(it.title)

                    layoutBook.imageViewTo.gone()
                    layoutBook.textViewTo.gone()
                    if (it.coverImage.isNotEmpty()) {
                        Glide.with(root.context).load(it.coverImage).into(layoutBook.imageViewBook)
                    } else {
                        Glide.with(root.context).load(R.drawable.ic_baseline_menu_book)
                            .into(layoutBook.imageViewBook)
                    }

                    editTextContents.setText(it.contents)
                    editTextContents.setSelection(it.contents.length)

                    editTextReview.setText(it.review)
                    editTextReview.setSelection(it.review.length)
                })

                readingBookDetailViewModel.isContentsPage.observe(viewLifecycleOwner, {
                    if (it) {
                        editTextContents.requestFocus()
                    } else {
                        editTextReview.requestFocus()
                    }
                })
            }

        return binding.root
    }

}