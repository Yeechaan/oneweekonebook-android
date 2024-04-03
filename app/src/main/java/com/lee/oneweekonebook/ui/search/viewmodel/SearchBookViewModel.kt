package com.lee.oneweekonebook.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBookList
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
    private val bookRequestRepository: BookRequestRepository,
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

            when (val result = bookRequestRepository.searchBook(query)) {
                is Result.Success -> {
                    val books = result.data.asBookList()
                    _uiState.value = SearchBookUiState.Success(books)
                    _isEmpty.value = books.isEmpty()
                }

                is Result.Error -> {
                    _uiState.value = SearchBookUiState.Error
                }
            }
        }
    }

}