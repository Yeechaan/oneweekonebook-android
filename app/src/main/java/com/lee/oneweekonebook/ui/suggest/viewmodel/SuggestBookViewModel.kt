package com.lee.oneweekonebook.ui.suggest.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBookList
import com.lee.oneweekonebook.ui.suggest.model.asBookList
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

    private val _bookInfo = MutableLiveData<BookInfo?>()
    val bookInfo: LiveData<BookInfo?>
        get() = _bookInfo

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    init {
        viewModelScope.launch {
            val categoryId = savedStateHandle.get<Int>("categoryId") ?: 101

            when (val result = bookRequestRepository.getRecommendationBook(categoryId)) {
                is Result.Success -> _books.value = result.data.asBookList()
                is Result.Error -> _error.value = result.errorCode
            }
        }
    }

    fun getBookInfo(isbn: String) = run {
        viewModelScope.launch {
            when (val result = bookRequestRepository.searchBookByISBN(isbn)) {
                is Result.Success -> {
                    _bookInfo.value = result.data.asBookList().first()
                }
                is Result.Error -> _error.value = result.errorCode
            }
        }
    }

    fun doneBoonInfo() {
        _bookInfo.value = null
    }
}