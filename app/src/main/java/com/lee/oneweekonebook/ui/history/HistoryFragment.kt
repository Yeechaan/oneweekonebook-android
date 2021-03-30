package com.lee.oneweekonebook.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentHistoryBinding
import com.lee.oneweekonebook.ui.done.DoneBookFragment
import com.lee.oneweekonebook.ui.reading.ReadingBookFragment
import com.lee.oneweekonebook.ui.wish.WishBookFragment
import com.lee.oneweekonebook.utils.ViewPagerTransformer

const val BOOK_WISH = 0
const val BOOK_READING = 1
const val BOOK_DONE = 2

class HistoryFragment : Fragment() {

    var binding: FragmentHistoryBinding? = null
    private val args by navArgs<HistoryFragmentArgs>()

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
            .apply {
                viewPagerHistory.apply {
                    adapter = HistoryAdapter(this@HistoryFragment)
                    setPageTransformer(ViewPagerTransformer())

                    setCurrentItem(args.bookType, false)
                }

                TabLayoutMediator(tabLayoutHistory, viewPagerHistory) { tab, position ->
                    tab.text = getTabTitle(position)
                }.attach()

            }

        return binding?.root
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            BOOK_WISH -> getString(R.string.wish_title)
            BOOK_READING -> getString(R.string.reading_title)
            BOOK_DONE -> getString(R.string.done_title)
            else -> null
        }
    }

    inner class HistoryAdapter(fragment: Fragment)
        : FragmentStateAdapter(fragment) {

        private val pageList: Map<Int, () -> Fragment> = mapOf(
            BOOK_WISH to { WishBookFragment() },
            BOOK_READING to { ReadingBookFragment() },
            BOOK_DONE to { DoneBookFragment() }
        )

        override fun getItemCount() = pageList.size

        override fun createFragment(position: Int) = pageList[position]?.invoke()
            ?: throw IndexOutOfBoundsException()
    }

}