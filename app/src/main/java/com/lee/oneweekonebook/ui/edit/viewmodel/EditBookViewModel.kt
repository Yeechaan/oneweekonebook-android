package com.lee.oneweekonebook.ui.edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.Book

class EditBookViewModel(val bookDao: BookDatabaseDao, val bookId: Int) : ViewModel() {

    val book = bookDao.getBook(bookId)

    fun editBook(book: Book) {
        bookDao.update(book)
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