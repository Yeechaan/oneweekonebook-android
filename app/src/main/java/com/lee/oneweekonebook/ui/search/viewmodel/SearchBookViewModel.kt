package com.lee.oneweekonebook.ui.search.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.repo.BookRequestRepository
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBookList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val bookRequestRepository: BookRequestRepository,
) : ViewModel() {

    private val appContext by lazy { context }

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    fun searchBook(query: String) {
        viewModelScope.launch {
            when (val result = bookRequestRepository.searchBook(query)) {
                is Result.Success -> _books.value = result.data.asBookList()
                is Result.Error -> Toast.makeText(appContext, appContext.getString(R.string.error_api_service), Toast.LENGTH_LONG).show()
            }
        }
    }

}