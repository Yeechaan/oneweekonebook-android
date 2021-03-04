package com.lee.oneweekonebook.utils

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.lee.oneweekonebook.databinding.ItemSnackbarBinding

class CustomSnackBar(view: View, private val message: String) {

    companion object {
        fun make(view: View, message: String) = CustomSnackBar(view, message)
    }

    private val snackBar = Snackbar.make(view, "", 5000)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val snackBarBinding = ItemSnackbarBinding.inflate(LayoutInflater.from(view.context))

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackBarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
    }

    private fun initData() {
        snackBarBinding.textViewTitle.text = message
        snackBarBinding.buttonConfirm.setOnClickListener {
            // do something..
        }
    }

    fun show() {
        snackBar.show()
    }

}