package com.lee.oneweekonebook.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.ui.search.SearchBookApi
import kotlinx.coroutines.launch

class SearchBookViewModel : ViewModel() {

    fun searchBook(query: String) {
        viewModelScope.launch {
            val response = SearchBookApi.searchBookApiService.getSearchBookAsync(query).await()
            Log.d("TTTT", response.toString())
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