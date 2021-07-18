package com.lee.oneweekonebook.ui.done.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.BOOK_TYPE_DONE
import com.lee.oneweekonebook.repo.BookRepository
import com.lee.oneweekonebook.utils.ioDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoneBookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    val books = bookRepository.getAllBookByTypeAsync(BOOK_TYPE_DONE)

    fun deleteBook(bookId: Int) {
        viewModelScope.launch(ioDispatcher) {
            bookRepository.deleteBookById(bookId)
        }
    }
}