package com.lee.oneweekonebook.ui.reading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.databinding.FragmentReadingBookDetailBinding
import com.lee.oneweekonebook.ui.MainActivity
import com.lee.oneweekonebook.ui.book.BookContentsFragment
import com.lee.oneweekonebook.ui.book.BookReviewFragment
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModel
import com.lee.oneweekonebook.ui.reading.viewmodel.ReadingBookDetailViewModelFactory
import com.lee.oneweekonebook.utils.ConfirmDialog
import com.lee.oneweekonebook.utils.ViewPagerTransformer
import com.orhanobut.logger.Logger

const val BOOK_CONTENTS = 0
const val BOOK_REVIEW = 1

class ReadingBookDetailFragment : Fragment() {

    var binding: FragmentReadingBookDetailBinding? = null
    private val args: ReadingBookDetailFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val currentBookId = args.bookId
        (activity as MainActivity).mainViewModel.setBookId(bookId = currentBookId)

        val application = requireNotNull(this.activity).application
        val bookDao = BookDatabase.getInstance(application).bookDatabaseDao

        val viewModelFactory = ReadingBookDetailViewModelFactory(bookDao, currentBookId)
        val readingBookDetailViewModel = ViewModelProvider(this, viewModelFactory).get(ReadingBookDetailViewModel::class.java)

        binding = FragmentReadingBookDetailBinding.inflate(inflater, container, false)
            .apply {
                viewModel = readingBookDetailViewModel
                lifecycleOwner = this@ReadingBookDetailFragment

//                viewPagerReadingBook.apply {
//                    adapter = ReadingBookDetailAdapter(this@ReadingBookDetailFragment)
//                    setPageTransformer(ViewPagerTransformer())
//                }
//                TabLayoutMediator(tabLayoutReadingBook, viewPagerReadingBook) { tab, position ->
//                    tab.text = getTabTitle(position)
//                }.attach()

                buttonDoneBook.setOnClickListener {
                    ConfirmDialog(
                        description = getString(R.string.dialog_book_done_description),
                        positiveMessage = getString(R.string.dialog_book_positive),
                        onConfirm = {
//                            readingBookDetailViewModel.doneReadingBook()
                            (activity as MainActivity).mainViewModel.doneReadingBook()
                            Logger.d((activity as MainActivity).mainViewModel.getCurrentBook())
                            findNavController().navigate(ReadingBookDetailFragmentDirections.actionReadingBookFragmentToHistoryBookFragment(bookType = BOOK_TYPE_DONE))
                        }
                    ).show(childFragmentManager, ConfirmDialog.TAG)
                }
//
//                buttonSaveBook.setOnClickListener {
//                    val contents = editTextTitle.text.toString()
//                    val review = editTextReview.text.toString()
//
//                    readingBookDetailViewModel.saveReadingBook(contents = contents, review = review)
//                    Toast.makeText(requireContext(), getString(R.string.reading_save), Toast.LENGTH_SHORT).show()
//                    findNavController().navigateUp()
//                }
//
//                readingBookDetailViewModel.book.observe(viewLifecycleOwner, {
//                    (activity as MainActivity).setToolbarTitle(it.title)
//                })
            }

        return binding?.root
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            BOOK_CONTENTS -> getString(R.string.reading_contents)
            BOOK_REVIEW -> getString(R.string.reading_review)
            else -> null
        }
    }

    inner class ReadingBookDetailAdapter(fragment: Fragment)
        : FragmentStateAdapter(fragment) {

        private val pageList: Map<Int, () -> Fragment> = mapOf(
            BOOK_CONTENTS to { BookContentsFragment() },
            BOOK_REVIEW to { BookReviewFragment() }
        )

        override fun getItemCount() = pageList.size

        override fun createFragment(position: Int) = pageList[position]?.invoke()
            ?: throw IndexOutOfBoundsException()
    }

}