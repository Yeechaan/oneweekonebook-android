package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.repo.BookRepository
import com.lee.oneweekonebook.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoneBookDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository
) : ViewModel() {

    val book = bookRepository.getBookByIdAsync(savedStateHandle["bookId"] ?: 0)

    val savedContents: String?
        get() = savedStateHandle["contents"]
    val contents = savedStateHandle.getLiveData<String>("contents")

    val savedReview: String?
        get() = savedStateHandle["review"]
    val review = savedStateHandle.getLiveData<String>("review")

    private val _isContentsPage = MutableLiveData(true)
    val isContentsPage: LiveData<Boolean>
        get() = _isContentsPage

    val bookPeriodFormat = Transformations.map(book) { book ->
        DateUtils().formatBookPeriod(book.startDate, book.endDate)
    }

    fun saveReadingBook() {
        viewModelScope.launch {
            book.value?.let { it ->
                it.contents = savedContents ?: ""
                it.review = savedReview ?: ""
                bookRepository.updateBook(it)
            }
        }
    }

    fun setCurrentPage(isContentsPage: Boolean) = run {
        _isContentsPage.value = isContentsPage
    }

}