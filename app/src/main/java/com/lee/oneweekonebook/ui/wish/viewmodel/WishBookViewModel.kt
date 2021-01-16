package com.lee.oneweekonebook.ui.wish.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.database.model.Book

class WishBookViewModel(bookDao: BookDatabaseDao, application: Application) : AndroidViewModel(application) {

    val books = bookDao.getBooksByType(BOOK_TYPE_WISH)

//    private val _books = MutableLiveData<List<Book>>()
//    val books: LiveData<List<Book>>
//        get() = _books
//
//    fun setBooks(books: List<Book>) {
//        _books.value = books
//    }

}

class WishBookViewModelFactory(
        private val bookDatabaseDao: BookDatabaseDao,
        private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishBookViewModel::class.java)) {
            return WishBookViewModel(bookDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}