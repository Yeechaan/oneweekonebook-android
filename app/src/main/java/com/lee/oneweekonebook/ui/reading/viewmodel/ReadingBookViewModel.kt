package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingBookViewModel(private val bookDao: BookDatabaseDao, private val bookId: Int) : ViewModel() {

    private lateinit var book: LiveData<Book>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val currentBook = bookDao.get(bookId)
            book = currentBook
        }
    }

    fun doneReadingBook(contents: String, review: String) {
        viewModelScope.launch(Dispatchers.IO) {
            book.value?.let { it ->
                it.contents = contents
                it.review = review
                bookDao.update(it)
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