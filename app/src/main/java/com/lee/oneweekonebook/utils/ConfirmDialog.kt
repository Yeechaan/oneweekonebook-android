package com.lee.oneweekonebook.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.lee.oneweekonebook.R

open class ConfirmDialog(
    private val onConfirm: () -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_book_add)
                .setPositiveButton(R.string.dialog_book_confirm,
                    DialogInterface.OnClickListener { dialog, id ->
                        onConfirm()
                        dismiss()
                    })
                .setNegativeButton(R.string.dialog_book_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dismiss()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}