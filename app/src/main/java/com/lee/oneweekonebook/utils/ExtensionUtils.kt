package com.lee.oneweekonebook.utils

import android.text.Editable
import com.google.gson.Gson


fun String.ifEmptyReturnNull() = if (isBlank()) null else this

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

fun <T> MutableList<T>.addElements(vararg elements: T) = elements.map { add(it) }

fun <T> List<T>.toJson(): String = run {
    val gson = Gson()
    gson.toJson(this)
}