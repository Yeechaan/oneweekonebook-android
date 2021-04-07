package com.lee.oneweekonebook.ui.edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditBookViewModel(val bookDao: BookDatabaseDao, val bookId: Int) : ViewModel() {

    val book = bookDao.getBookAsync(bookId)

    fun editBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentBook = bookDao.getBook(bookId)

            currentBook.apply {
                title = book.title
                writer = book.writer
                publisher = book.publisher
            }
            bookDao.update(currentBook)
        }
    }

}

class EditBookViewModelFactory(
    private val bookDao: BookDatabaseDao,
    private val bookId: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditBookViewModel::class.java)) {
            return EditBookViewModel(bookDao, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}