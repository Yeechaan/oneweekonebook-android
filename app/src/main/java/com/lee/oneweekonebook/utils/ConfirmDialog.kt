package com.lee.oneweekonebook.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lee.oneweekonebook.R

open class ConfirmDialog(
    private val description: String,
    private val positiveMessage: String = "",
    private val negativeMessage: String = "",
    private val onConfirm: () -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val positiveMessage = positiveMessage.ifEmptyReturnNull() ?: getString(R.string.dialog_book_positive)
            val negativeMessage = negativeMessage.ifEmptyReturnNull() ?: getString(R.string.dialog_book_negative)

            val builder = AlertDialog.Builder(it)
            builder.setMessage(description)
                .setPositiveButton(positiveMessage
                ) { _, _ ->
                    onConfirm()
                    dismiss()
                }
                .setNegativeButton(negativeMessage
                ) { _, _ ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "ConfirmDialog"
    }
}