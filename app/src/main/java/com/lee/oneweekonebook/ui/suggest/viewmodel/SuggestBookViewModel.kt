package com.lee.oneweekonebook.ui.suggest.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.ui.search.SearchBookApi
import com.lee.oneweekonebook.ui.suggest.model.SuggestBook
import com.lee.oneweekonebook.ui.suggest.model.asBookList
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class SuggestBookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<SuggestBook>>()
    val books: LiveData<List<SuggestBook>>
        get() = _books

    init {
        viewModelScope.launch {
            val response = SearchBookApi.searchBookApiService.getBookImageAsync("미움", 10).await()
            _books.value = response.asBookList()
            Logger.d(response)
        }

    }
}

class SuggestBookViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuggestBookViewModel::class.java)) {
            return SuggestBookViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}