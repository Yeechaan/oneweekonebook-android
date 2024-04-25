package com.lee.oneweekonebook.ui.wish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.mapper.BookDomain
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishBookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    val books = bookRepository.getBooks(BOOK_TYPE_WISH).asLiveData()

    fun addReadingBook(bookId: Int) {
        viewModelScope.launch {
            bookRepository.updateBookType(bookId, BOOK_TYPE_READING)
        }
    }

    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            bookRepository.deleteBook(bookId)
        }
    }

}