package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingBookDetailViewModel(val bookDao: BookDatabaseDao, val bookId: Int) : ViewModel() {

    val book = bookDao.getBookAsync(bookId)

    fun saveReadingBook(contents: String, review: String) {
        viewModelScope.launch(Dispatchers.IO) {
            book.value?.let { it ->
                it.contents = contents
                it.review = review
                bookDao.update(it)
            }
        }
    }

    fun doneReadingBook(contents: String, review: String) {
        viewModelScope.launch(Dispatchers.IO) {
            book.value?.let { it ->
                it.contents = contents
                it.review = review
                it.type = BOOK_TYPE_DONE
                bookDao.update(it)
            }
        }
    }
}

class ReadingBookDetailViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
    private val bookId: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadingBookDetailViewModel::class.java)) {
            return ReadingBookDetailViewModel(bookDatabaseDao, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}