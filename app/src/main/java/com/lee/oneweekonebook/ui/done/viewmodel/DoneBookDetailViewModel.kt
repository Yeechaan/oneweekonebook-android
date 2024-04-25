package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoneBookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val book = bookRepository.getBook(savedStateHandle["bookId"] ?: 0).asLiveData()

    val savedContents: String?
        get() = savedStateHandle["contents"]
    val contents = savedStateHandle.getLiveData<String>("contents")

    val savedReview: String?
        get() = savedStateHandle["review"]
    val review = savedStateHandle.getLiveData<String>("review")

    private val _isContentsPage = MutableLiveData(true)
    val isContentsPage: LiveData<Boolean>
        get() = _isContentsPage

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