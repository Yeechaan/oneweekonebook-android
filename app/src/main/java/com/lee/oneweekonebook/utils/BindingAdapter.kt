package com.lee.oneweekonebook.utils

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.common.ItemClickListener

@BindingAdapter(value = ["items", "listener"])
fun ViewGroup.addPickerItem(
    items: List<String>?,
    listener: ItemClickListener?
) {
    removeAllViews()
    items?.forEachIndexed { index, item ->
        addView(
            MaterialButton(context, null, R.attr.itemPickerStyle)
                .apply {
                    text = item
                    setOnClickListener { listener?.onClick(index, item) }
                }
        )
    }
}