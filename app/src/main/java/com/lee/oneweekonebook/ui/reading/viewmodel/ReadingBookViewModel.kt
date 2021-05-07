package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.utils.ioDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingBookViewModel(val bookDao: BookDatabaseDao) : ViewModel() {

    val books = bookDao.getBooksByType(BOOK_TYPE_READING)

    fun deleteBook(bookId: Int) {
        viewModelScope.launch(ioDispatcher) {
            bookDao.deleteBook(bookId)
        }
    }
}

class ReadingBookViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadingBookViewModel::class.java)) {
            return ReadingBookViewModel(bookDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}