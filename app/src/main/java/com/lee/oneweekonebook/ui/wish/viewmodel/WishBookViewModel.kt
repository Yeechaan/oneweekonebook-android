package com.lee.oneweekonebook.ui.wish.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WishBookViewModel(private val bookDao: BookDatabaseDao, application: Application) : AndroidViewModel(application) {

    val books = bookDao.getBooksByType(BOOK_TYPE_WISH)

    fun addReadingBook(bookId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentBook = bookDao.getBook(bookId)

            currentBook.apply {
                type = BOOK_TYPE_READING
            }
            bookDao.update(currentBook)
        }
    }

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