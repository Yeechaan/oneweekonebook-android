package com.lee.oneweekonebook.ui.suggest.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.suggest.model.asBookList
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestBookViewModel @Inject constructor(
    private val bookRequestRepository: BookRequestRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    init {
        viewModelScope.launch {
            try {
                val categoryId = savedStateHandle.get<Int>("categoryId") ?: 101
                val responseCategory = bookRequestRepository.getRecommendationBook(categoryId)
                Logger.d(responseCategory)
                _books.value = responseCategory.asBookList()
            } catch (e: Exception) {
                Logger.d(e.message)
            }
        }
    }

}