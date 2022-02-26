package com.lee.oneweekonebook.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBookList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val bookRequestRepository: BookRequestRepository,
) : ViewModel() {

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error


    fun searchBook(query: String) {
        viewModelScope.launch {
            when (val result = bookRequestRepository.searchBook(query)) {
                is Result.Success -> _books.value = result.data.asBookList()
                is Result.Error -> _error.value = result.errorCode
            }
        }
    }

}