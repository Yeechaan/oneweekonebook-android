package com.lee.oneweekonebook.ui.book.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.repository.BookRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * State 관리를 어떻게 하는게 최선일까?
 * sealed interface, class : 상태를 제한할 수 있고, 각 상태에 따라 내부에 여러 값을 전달할 수 있다(또는 object 전달하지 않음)
 * data class : 상태를 제한할 수 없지만, 각 필드에서 상태 값들을 저장하여 관리할 수 있다
 * 분명 Compose로 화면을 구성했을 때는 sealed로 제한하는게 편했지만, xml에서는 까다롭다(ex. loading progress 처럼 모든 UI 컴포넌트가 한 화면에 있기때문에)
 * **/
data class BookDetailUiState(
    val bookInfo: BookInfo? = null,
    val savedBookType: Int? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState>
        get() = _uiState

    init {
        val isbn = savedStateHandle.get<String>("isbn") ?: ""
        getBook(isbn)
    }

    private fun getBook(isbn: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val result = bookRepository.searchBookByISBN(isbn)
            result.fold(
                onSuccess = { bookInfo ->
                    _uiState.update { it.copy(bookInfo = bookInfo, loading = false) }
                },
                onFailure = {
                    _uiState.update { it.copy(errorMessage = "서비스 연결에 오류가 있습니다\\n지속적인 문제가 발생하면 관리자에게 문의해 주세요", loading = false) }
                }
            )
        }
    }

    fun addBook(type: @BookType Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }

            val bookInfo = _uiState.value.bookInfo ?: BookInfo()

            val isSameBookSaved = bookRepository.isSameBookSaved(bookInfo.isbn)
            if (isSameBookSaved) {
                _uiState.update { it.copy(savedBookType = null, errorMessage = "독서내역에 추가된 책 입니다!", loading = false) }
                return@launch
            }

            bookRepository.saveBook(bookInfo)
            _uiState.update { it.copy(savedBookType = type, loading = false) }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

}