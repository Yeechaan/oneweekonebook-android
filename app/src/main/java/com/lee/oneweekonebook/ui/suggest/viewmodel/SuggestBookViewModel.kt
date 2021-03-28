package com.lee.oneweekonebook.ui.suggest.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.network.BookApi
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.suggest.model.asBookList
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

class SuggestBookViewModel(val categoryId: Int) : ViewModel() {

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    init {
        viewModelScope.launch {
            // 책 추천
//            val responseCategory = BookApi.bookApiService.getSuggestBookAsync(categoryId = RecommendBook().getRandomCategory()).await()
//            Logger.d(responseCategory)
//            _books.value = responseCategory.asBookList()

            val responseCategory = BookApi.bookApiService.getSuggestBookAsync(categoryId = categoryId).await()
            Logger.d(responseCategory)
            _books.value = responseCategory.asBookList()
        }
    }

    fun refreshBooks() {
        viewModelScope.launch {
            // 책 추천
//            val responseCategory = BookApi.bookApiService.getSuggestBookAsync(categoryId = RecommendBook().getRandomCategory()).await()
//            Logger.d(responseCategory)
//            _books.value = responseCategory.asBookList()

            val responseCategory = BookApi.bookApiService.getSuggestBookAsync(categoryId = categoryId).await()
            Logger.d(responseCategory)
            _books.value = responseCategory.asBookList()
        }
    }

}

class SuggestBookViewModelFactory(
    private val categoryId: Int,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuggestBookViewModel::class.java)) {
            return SuggestBookViewModel(categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}