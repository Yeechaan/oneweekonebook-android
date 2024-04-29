package com.lee.oneweekonebook.ui.wish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.mapper.BookDomain
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WishBookUiState(
    val books: List<BookDomain>? = null,
    var loading: Boolean = false,
)

@HiltViewModel
class WishBookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishBookUiState())
    val uiState: StateFlow<WishBookUiState>
        get() = _uiState

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            bookRepository.getBooks(BOOK_TYPE_WISH).collectLatest { books ->
                _uiState.update { it.copy(books = books, loading = false) }
            }
        }
    }

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