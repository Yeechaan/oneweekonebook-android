package com.lee.oneweekonebook.utils

import android.text.Editable
import android.view.View
import com.google.gson.Gson


fun String.ifEmptyReturnNull() = if (isBlank()) null else this

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun <T> MutableList<T>.addElements(vararg elements: T) = elements.map { add(it) }

fun <T> List<T>.toJson(): String = run {
    val gson = Gson()
    gson.toJson(this)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}