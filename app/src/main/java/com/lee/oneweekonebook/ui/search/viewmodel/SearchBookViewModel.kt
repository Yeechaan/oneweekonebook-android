package com.lee.oneweekonebook.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.network.BookApi
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBookList
import com.lee.oneweekonebook.utils.ioDispatcher
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val bookRequestRepository: BookRequestRepository,
) : ViewModel() {

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    fun searchBook(query: String) {
        viewModelScope.launch {
            try {
//                val response = withContext(ioDispatcher) {
//                    BookApi.bookApiService.searchBook(query = query)
//                }
                val response = bookRequestRepository.searchBook(query)
                _books.value = response.asBookList()
            } catch (e: Exception) {
                Logger.d(e.message)
            }
        }
    }

}