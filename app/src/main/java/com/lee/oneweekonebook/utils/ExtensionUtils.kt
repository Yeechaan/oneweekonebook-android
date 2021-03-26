package com.lee.oneweekonebook.utils

import com.google.gson.Gson


fun String.ifEmptyReturnNull() = if (isBlank()) null else this

fun <T> MutableList<T>.addElements(vararg elements: T) = elements.map { add(it) }

fun <T> List<T>.toJson(): String = run {
    val gson = Gson()
    gson.toJson(this)
}