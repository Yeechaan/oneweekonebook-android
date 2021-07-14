package com.lee.oneweekonebook.ui.home.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    val isBookEmpty = Transformations.map(books) {
        it.isNullOrEmpty()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _books.postValue(
                BookDatabase.getInstance(context).bookDatabaseDao.getBooksType(
                    BOOK_TYPE_READING
                )
            )
        }
    }

}