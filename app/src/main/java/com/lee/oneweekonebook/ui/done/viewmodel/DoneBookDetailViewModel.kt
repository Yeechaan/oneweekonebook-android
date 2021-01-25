package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoneBookDetailViewModel(private val bookDao: BookDatabaseDao, private val bookId: Int) : ViewModel() {

    lateinit var book: LiveData<Book>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val currentBook = bookDao.getBookAsync(bookId)
            book = currentBook
        }
    }

//    private val _book = MutableLiveData<Book>()
//    val book: LiveData<Book>
//        get() = _book

    fun getBook(bookId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
//            val currentBook = bookDao.getBook(bookId)
//            _book.postValue(currentBook)
            book = bookDao.getBookAsync(bookId)
        }
    }

}

class DoneBookDetailViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
    private val bookId: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoneBookDetailViewModel::class.java)) {
            return DoneBookDetailViewModel(bookDatabaseDao, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}