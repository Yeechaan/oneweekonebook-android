package com.lee.oneweekonebook.ui.reading.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BookType
import com.lee.oneweekonebook.utils.ioDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadingBookDetailViewModel(val bookDao: BookDatabaseDao, val bookId: Int) : ViewModel() {

    val book = bookDao.getBookAsync(bookId)

    private val _isContentsPage = MutableLiveData(true)
    val isContentsPage: LiveData<Boolean>
        get() = _isContentsPage

    fun saveBook(type: @BookType Int, contents: String, review: String) {
        viewModelScope.launch(ioDispatcher) {
            book.value?.let { it ->
                it.contents = contents
                it.review = review

                when (type) {
                    BOOK_TYPE_READING -> {
                        // TODO 독서시간 저장
                    }
                    BOOK_TYPE_DONE -> {
                        it.type = BOOK_TYPE_DONE
                        it.endDate = System.currentTimeMillis()
                    }
                }
                bookDao.update(it)
            }
        }
    }

    fun setCurrentPage(isContentsPage: Boolean) = run {
        _isContentsPage.value = isContentsPage
    }

}


class ReadingBookDetailViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
    private val bookId: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadingBookDetailViewModel::class.java)) {
            return ReadingBookDetailViewModel(bookDatabaseDao, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}