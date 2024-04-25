package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoneBookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    val books = bookRepository.getBooks(BOOK_TYPE_DONE).asLiveData()

    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            bookRepository.deleteBook(bookId)
        }
    }
}