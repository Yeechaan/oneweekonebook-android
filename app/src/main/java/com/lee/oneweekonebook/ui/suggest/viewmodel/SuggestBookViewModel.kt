package com.lee.oneweekonebook.ui.suggest.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.suggest.model.asBookList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestBookViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val bookRequestRepository: BookRequestRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val appContext by lazy { context }

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    init {
        viewModelScope.launch {
            val categoryId = savedStateHandle.get<Int>("categoryId") ?: 101

            when (val result = bookRequestRepository.getRecommendationBook(categoryId)) {
                is Result.Success -> _books.value = result.data.asBookList()
                is Result.Error -> Toast.makeText(appContext, appContext.getString(R.string.error_api_service), Toast.LENGTH_LONG).show()
            }
        }
    }

}