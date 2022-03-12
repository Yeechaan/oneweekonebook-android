package com.lee.oneweekonebook.ui.wish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.repo.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    val books = bookRepository.getAllBookByTypeAsync(BOOK_TYPE_WISH).asLiveData()

    fun addReadingBook(bookId: Int) {
        viewModelScope.launch {
            val currentBook = bookRepository.getBookById(bookId)
            currentBook.apply {
                type = BOOK_TYPE_READING
            }
            bookRepository.updateBook(currentBook)
        }
    }

    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            bookRepository.deleteBookById(bookId)
        }
    }

}