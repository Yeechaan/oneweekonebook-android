package com.lee.oneweekonebook.ui.book.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.repo.BookRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _isBookSaved = MutableLiveData<Int>()
    val isBookSaved: LiveData<Int>
        get() = _isBookSaved

    fun addBook(type: @BookType Int, bookInfo: BookInfo) {
        val book = bookInfo.asBook()
        book.type = type

        viewModelScope.launch {
            if (!bookRepository.isSameBookSaved(bookInfo.title)) {
                bookRepository.addBook(book)
                _isBookSaved.postValue(type)
            } else {
                _isBookSaved.postValue(BOOK_TYPE_UNKNOWN)
            }
        }
    }

}