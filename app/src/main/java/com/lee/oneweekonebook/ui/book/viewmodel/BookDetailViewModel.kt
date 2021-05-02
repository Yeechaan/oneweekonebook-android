package com.lee.oneweekonebook.ui.book.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBook
import com.lee.oneweekonebook.utils.ioDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookDetailViewModel(val bookDao: BookDatabaseDao) : ViewModel() {

    private val _isBookSaved = MutableLiveData<Int>()
    val isBookSaved: LiveData<Int>
        get() = _isBookSaved

    fun addBook(type: @BookType Int, bookInfo: BookInfo) {
        val book = bookInfo.asBook()
        book.type = type

        viewModelScope.launch(ioDispatcher) {
            val bookAlreadySaved = bookDao.getBookWithTitle(bookInfo.title)

            if (bookAlreadySaved == null) {
                bookDao.insert(book)
                _isBookSaved.postValue(type)
            } else {
                _isBookSaved.postValue(BOOK_TYPE_UNKNOWN)
            }
        }
    }

}

class BookDetailViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            return BookDetailViewModel(bookDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}