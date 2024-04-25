package com.lee.oneweekonebook.ui.suggest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.repository.BookRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SuggestBookUiState {
    data class Success(val books: List<BookInfo>) : SuggestBookUiState
    data class Error(val message: String) : SuggestBookUiState
    object Loading : SuggestBookUiState
}

@HiltViewModel
class SuggestBookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SuggestBookUiState>(SuggestBookUiState.Loading)
    val uiState: StateFlow<SuggestBookUiState>
        get() = _uiState

    init {
        val categoryId = savedStateHandle.get<Int>("categoryId") ?: 101
        getBooks(categoryId)
    }

    private fun getBooks(categoryId: Int) {
        viewModelScope.launch {
            val result = bookRepository.searchRecommendationBooks(categoryId)
            result.fold(
                onSuccess = {
                    _uiState.value = SuggestBookUiState.Success(it)
                },
                onFailure = {
                    _uiState.value = SuggestBookUiState.Error("서비스 연결에 오류가 있습니다\\n지속적인 문제가 발생하면 관리자에게 문의해 주세요")
                }
            )
        }
    }

}