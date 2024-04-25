package com.lee.oneweekonebook.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.repository.BookRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchBookUiState {
    data class Success(val books: List<BookInfo>) : SearchBookUiState
    object Error : SearchBookUiState
    object Loading : SearchBookUiState
    object Init : SearchBookUiState
}

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchBookUiState>(SearchBookUiState.Init)
    val uiState: StateFlow<SearchBookUiState>
        get() = _uiState

    private val _isEmpty = MutableStateFlow<Boolean>(true)
    val isEmpty: StateFlow<Boolean>
        get() = _isEmpty

    fun searchBook(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchBookUiState.Loading

            val result = bookRepository.searchBookByTitle(query)
            result.fold(
                onSuccess = { books ->
                    _uiState.value = SearchBookUiState.Success(books)
                    _isEmpty.value = books.isEmpty()
                },
                onFailure = { exception ->
                    exception.message
                    _uiState.value = SearchBookUiState.Error
                }
            )
        }
    }

}