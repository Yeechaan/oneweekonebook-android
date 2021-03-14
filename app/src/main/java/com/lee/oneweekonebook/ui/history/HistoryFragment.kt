package com.lee.oneweekonebook.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentHistoryBinding
import com.lee.oneweekonebook.ui.done.DoneBookFragment
import com.lee.oneweekonebook.ui.reading.ReadingBookFragment
import com.lee.oneweekonebook.ui.wish.WishBookFragment

const val BOOK_WISH = 0
const val BOOK_READING = 1
const val BOOK_DONE = 2

class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
            .apply {
                viewPagerHistory.adapter = HistoryAdapter(this@HistoryFragment)

                TabLayoutMediator(tabLayoutHistory, viewPagerHistory) { tab, position ->
                    tab.text = getTabTitle(position)
                }.attach()
            }

        return binding.root
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