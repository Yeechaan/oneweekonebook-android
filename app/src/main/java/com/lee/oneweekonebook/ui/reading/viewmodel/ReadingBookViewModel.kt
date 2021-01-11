package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingBookViewModel(private val bookDao: BookDatabaseDao, private val bookId: Int) : ViewModel() {

    fun doneReadingBook(contents: String, review: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val readingBook = bookDao.get(bookId)
            readingBook?.let { book ->
                book.contents = contents
                book.review = review
                bookDao.update(book)
            }
        }
    }

}

class ReadingBookViewModelFactory(
        private val bookDatabaseDao: BookDatabaseDao,
        private val bookId: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadingBookViewModel::class.java)) {
            return ReadingBookViewModel(bookDatabaseDao, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}