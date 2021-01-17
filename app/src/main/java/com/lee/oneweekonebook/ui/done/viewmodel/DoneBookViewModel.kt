package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE

class DoneBookViewModel(bookDao: BookDatabaseDao) : ViewModel() {

    val books = bookDao.getBooksByType(BOOK_TYPE_DONE)

}

class DoneBookViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
    ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoneBookViewModel::class.java)) {
            return DoneBookViewModel(bookDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}