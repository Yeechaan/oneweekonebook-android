package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.repo.BookRepository
import com.lee.oneweekonebook.utils.ioDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingBookDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository
) : ViewModel() {

    val book = bookRepository.getBookByIdAsync(savedStateHandle["bookId"] ?: 0)

    private val _isContentsPage = MutableLiveData(true)
    val isContentsPage: LiveData<Boolean>
        get() = _isContentsPage

    fun saveBook(type: @BookType Int, contents: String, review: String) {
        viewModelScope.launch(ioDispatcher) {
            book.value?.let { it ->
                it.contents = contents
                it.review = review

                when (type) {
                    BOOK_TYPE_READING -> {
                        // 추후 독서시간 저장 기능 구현
                    }
                    BOOK_TYPE_DONE -> {
                        it.type = BOOK_TYPE_DONE
                        it.endDate = System.currentTimeMillis()
                    }
                }
                bookRepository.updateBook(it)
            }
        }
    }

    fun setCurrentPage(isContentsPage: Boolean) = run {
        _isContentsPage.value = isContentsPage
    }

}