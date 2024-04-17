package com.lee.oneweekonebook.common

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lee.oneweekonebook.databinding.BottomDialogItemPickerBinding

class ItemPickerBottomDialog(
    private val title: String,
    private val items: List<String>,
    private val onPick: (Int, String) -> Unit = { _, _ -> },
    private val onDismiss: () -> Unit = {},
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = BottomDialogItemPickerBinding.inflate(inflater, container, false)
            .apply {
                val clickListener = object : ItemClickListener {
                    override fun onClick(index: Int, item: String) {
                        onPick.invoke(index, item)
                        dismiss()
                    }
                }

                listener = clickListener
                items = this@ItemPickerBottomDialog.items

                textViewTitle.text = title
                imageViewCancel.setOnClickListener { dismiss() }
            }
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }
}

interface ItemClickListener {
    fun onClick(index: Int, item: String)
}