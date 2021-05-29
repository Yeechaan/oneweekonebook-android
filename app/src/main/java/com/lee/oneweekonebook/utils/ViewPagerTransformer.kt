package com.lee.oneweekonebook.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ViewPagerTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            alpha = when {
                position <= -1f || position >= 1f -> {
                    0f
                }
                position == 0f -> {
                    1f
                }
                else -> {
                    1f - Math.abs(position)
                }
            }
        }
    }
}