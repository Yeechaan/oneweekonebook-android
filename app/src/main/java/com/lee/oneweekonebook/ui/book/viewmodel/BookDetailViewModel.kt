package com.lee.oneweekonebook.ui.book.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookDetailViewModel(val bookDao: BookDatabaseDao) : ViewModel() {

    fun addBook(type: @BookType Int, bookInfo: BookInfo) {
        val book = bookInfo.asBook()
        book.type = type

        viewModelScope.launch(Dispatchers.IO) {
            bookDao.insert(book)
        }
    }
}

class BookDetailViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            return BookDetailViewModel(bookDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}