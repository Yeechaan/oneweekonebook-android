package com.lee.oneweekonebook.ui.edit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.mapper.BookDomain
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditBookUiState(
    var book: BookDomain? = null,
    val editSuccess: Boolean = false,
    val loading: Boolean = false,
)

@HiltViewModel
class EditBookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditBookUiState>(EditBookUiState())
    val uiState: StateFlow<EditBookUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            val bookId = savedStateHandle["bookId"] ?: 0
            bookRepository.getBook(bookId).collectLatest { book ->
                _uiState.update { it.copy(book = book) }
            }
        }
    }

    fun editBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            _uiState.value.book?.let {
                bookRepository.updateBook(it)
            }
            _uiState.update { it.copy(editSuccess = true, loading = false) }
        }
    }

}