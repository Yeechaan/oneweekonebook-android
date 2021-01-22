package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingBookViewModel(private val bookDao: BookDatabaseDao, private val bookId: Int) : ViewModel() {

    lateinit var book: LiveData<Book>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val currentBook = bookDao.getBookAsync(bookId)
            book = currentBook
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