package com.lee.oneweekonebook.utils

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.common.ItemClickListener
import com.lee.oneweekonebook.ui.search.viewmodel.SearchBookUiState

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

@BindingAdapter("show")
fun CircularProgressIndicator.bindShow(uiState: SearchBookUiState) {
    visibility = if (uiState is SearchBookUiState.Loading) View.VISIBLE else View.GONE
}