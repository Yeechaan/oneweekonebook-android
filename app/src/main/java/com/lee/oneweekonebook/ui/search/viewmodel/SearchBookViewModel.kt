package com.lee.oneweekonebook.ui.search.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.network.BookApi
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBookList
import com.lee.oneweekonebook.utils.ioDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    fun searchBook(query: String) {
        viewModelScope.launch {
            try {
                val response = withContext(ioDispatcher) {
                    BookApi.bookApiService.searchBookAsync(query = query).await()
                }
                _books.value = response.asBookList()
            } catch (e: Exception) {
            }
        }
    }

}