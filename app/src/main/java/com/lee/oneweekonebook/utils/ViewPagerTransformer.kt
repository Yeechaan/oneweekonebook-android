package com.lee.oneweekonebook.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ViewPagerTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position <= -1f || position >= 1f -> {
                    alpha = 0f
                }
                position == 0f -> {
                    alpha = 1f
                }
                else -> {
                    alpha = 1f - Math.abs(position)
                }
            }
        }
    }
}