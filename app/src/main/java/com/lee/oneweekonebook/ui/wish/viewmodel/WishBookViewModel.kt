package com.lee.oneweekonebook.ui.wish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.oneweekonebook.database.model.Book

class WishBookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    fun setBooks(books: List<Book>) {
        _books.value = books
    }
}

class WishBookViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishBookViewModel::class.java)) {
            return WishBookViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}