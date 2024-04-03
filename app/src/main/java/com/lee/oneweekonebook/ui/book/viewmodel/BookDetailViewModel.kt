package com.lee.oneweekonebook.ui.book.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.repo.BookRepository
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BookDetailUiState {
    data class Success(val bookInfo: BookInfo) : BookDetailUiState
    data class Saved(val bookType: Int) : BookDetailUiState
    object Loading : BookDetailUiState
    object Error : BookDetailUiState
}


@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val bookRequestRepository: BookRequestRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val uiState: StateFlow<BookDetailUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            val isbn = savedStateHandle.get<String>("isbn") ?: ""
            val book = bookRequestRepository.searchBookByISBN(isbn)
            when (book) {
                is Result.Success -> {
                    val bookInfo = book.data.item.firstOrNull()?.asBook() ?: BookInfo()
                    _uiState.value = BookDetailUiState.Success(bookInfo)
                }

                is Result.Error -> {
                    _uiState.value = BookDetailUiState.Error
                }
            }
        }
    }

    fun addBook(type: @BookType Int, bookInfo: BookInfo) {
        _uiState.value = BookDetailUiState.Loading

        viewModelScope.launch {
            val isSameBookSaved = bookRepository.isSameBookSaved(bookInfo.title)
            if (isSameBookSaved) {
                _uiState.value = BookDetailUiState.Saved(BOOK_TYPE_UNKNOWN)
            } else {
                val book = bookInfo.asBook(type)

                bookRepository.addBook(book)
                _uiState.value = BookDetailUiState.Saved(book.type)
            }
        }
    }

    fun clearState() {
        _uiState.value = BookDetailUiState.Loading
    }

}