package com.lee.oneweekonebook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(val bookDao: BookDatabaseDao) : ViewModel() {

    private var currentBookId: Int = 0
    private lateinit var book: Book

    fun saveBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            bookDao.update(book)
        }
    }

    fun setBookId(bookId: Int) {
        currentBookId = bookId

        viewModelScope.launch(Dispatchers.IO) {
            book = bookDao.getBook(bookId)
        }
    }

    fun getCurrentBook() = book

    fun doneReadingBook() {
        viewModelScope.launch(Dispatchers.IO) {
            book.let {
                it.type = BOOK_TYPE_DONE
                it.endDate = System.currentTimeMillis()
                bookDao.update(it)
            }
        }
    }

}

class MainViewModelFactory(
    private val bookDao: BookDatabaseDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(bookDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}