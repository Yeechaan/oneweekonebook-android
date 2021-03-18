package com.lee.oneweekonebook.ui.search.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.network.BookApi
import com.lee.oneweekonebook.ui.search.model.SearchBook
import com.lee.oneweekonebook.ui.search.model.asBookList
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class SearchBookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<SearchBook>>()
    val books: LiveData<List<SearchBook>>
        get() = _books

    fun searchBook(query: String) {
        viewModelScope.launch {
            val response = BookApi.bookApiService.searchBookAsync(query = query).await()

            _books.value = response.asBookList()
            Logger.d(response)
        }
    }
}

class SearchBookViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBookViewModel::class.java)) {
            return SearchBookViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}