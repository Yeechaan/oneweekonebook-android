package com.lee.oneweekonebook.ui.add.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.repo.BookRepository
import com.lee.oneweekonebook.utils.ioDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book>
        get() = _book

    fun getBook(bookId: Int) {
        viewModelScope.launch(ioDispatcher) {
            val currentBook = bookRepository.getBookById(bookId)
            _book.postValue(currentBook)
        }
    }

    fun saveBook(book: Book) {
        viewModelScope.launch(ioDispatcher) {
            bookRepository.addBook(book)
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch(ioDispatcher) {
            bookRepository.updateBook(book)
        }
    }

}