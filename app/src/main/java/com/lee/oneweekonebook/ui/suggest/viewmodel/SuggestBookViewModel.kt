package com.lee.oneweekonebook.ui.suggest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.suggest.model.asBookList
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SuggestBookUiState {
    data class Success(val books: List<BookInfo>) : SuggestBookUiState
    object Error : SuggestBookUiState
    object Loading : SuggestBookUiState
}

@HiltViewModel
class SuggestBookViewModel @Inject constructor(
    private val bookRequestRepository: BookRequestRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SuggestBookUiState>(SuggestBookUiState.Loading)
    val uiState: StateFlow<SuggestBookUiState>
        get() = _uiState


    init {
        viewModelScope.launch {
            val categoryId = savedStateHandle.get<Int>("categoryId") ?: 101

            when (val result = bookRequestRepository.getRecommendationBook(categoryId)) {
                is Result.Success -> {
                    _uiState.value = SuggestBookUiState.Success(result.data.asBookList())
                }

                is Result.Error -> {
                    _uiState.value = SuggestBookUiState.Error
                }
            }
        }
    }

}