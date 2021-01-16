package com.lee.oneweekonebook.ui.add.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBookViewModel(private val bookDao: BookDatabaseDao) : ViewModel() {

    fun saveBook(book: Book) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookDao.insert(book)
            }
        }
    }

}

class AddBookViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBookViewModel::class.java)) {
            return AddBookViewModel(bookDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}