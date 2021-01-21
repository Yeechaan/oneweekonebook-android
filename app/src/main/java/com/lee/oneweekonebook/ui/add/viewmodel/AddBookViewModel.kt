package com.lee.oneweekonebook.ui.add.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBookViewModel(private val bookDao: BookDatabaseDao) : ViewModel() {

//    private val _book = MutableLiveData<Book>()
//    var book: LiveData<Book>
//        get() = _book

    fun getBook(bookId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentBook = bookDao.get(bookId)
//            book = currentBook
        }
    }

    fun saveBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
                bookDao.insert(book)
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