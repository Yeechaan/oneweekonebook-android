package com.lee.oneweekonebook.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.lee.oneweekonebook.R
import kotlin.math.abs

fun ViewPager2.addPreview() {
    offscreenPageLimit = 1

    val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
    val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (0.25f * abs(position))
    }
    setPageTransformer(pageTransformer)

    val itemDecoration = PagerHorizontalMarginItemDecorator(
        context,
        R.dimen.viewpager_current_item_horizontal_margin
    )
    addItemDecoration(itemDecoration)
}