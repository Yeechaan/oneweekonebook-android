package com.lee.oneweekonebook.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lee.oneweekonebook.R

open class ConfirmDialog(
    private val description: String,
    private val positiveMessage: String,
    private val negativeMessage: String = "",
    private val onConfirm: () -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val negativeMessage = negativeMessage.ifEmptyReturnNull() ?: getString(R.string.dialog_book_negative)

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(description)
                .setPositiveButton(positiveMessage,
                    DialogInterface.OnClickListener { dialog, id ->
                        onConfirm()
                        dismiss()
                    })
                .setNegativeButton(negativeMessage,
                    DialogInterface.OnClickListener { dialog, id ->
                        dismiss()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "ConfirmDialog"
    }
}