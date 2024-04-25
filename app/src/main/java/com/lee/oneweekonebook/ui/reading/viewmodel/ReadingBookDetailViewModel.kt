package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingBookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val book = bookRepository.getBook(savedStateHandle["bookId"] ?: 0).asLiveData()

    private val _isContentsPage = MutableLiveData(true)
    val isContentsPage: LiveData<Boolean>
        get() = _isContentsPage


    val savedContents: String?
        get() = savedStateHandle["contents"]
    val contents = savedStateHandle.getLiveData<String>("contents")

    val savedReview: String?
        get() = savedStateHandle["review"]
    val review = savedStateHandle.getLiveData<String>("review")

    fun saveBook(type: @BookType Int) {
        book.value?.let { it ->
            it.contents = savedContents ?: ""
            it.review = savedReview ?: ""

            when (type) {
                BOOK_TYPE_READING -> {
                    // 추후 독서시간 저장 기능 구현
                }

                BOOK_TYPE_DONE -> {
                    it.type = BOOK_TYPE_DONE
                    it.endDate = System.currentTimeMillis()
                }
            }

            viewModelScope.launch {
                bookRepository.updateBook(it)
            }
        }
    }

    fun setCurrentPage(isContentsPage: Boolean) = run {
        _isContentsPage.value = isContentsPage
    }

}