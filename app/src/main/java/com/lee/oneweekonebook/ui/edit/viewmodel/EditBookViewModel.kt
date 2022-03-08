package com.lee.oneweekonebook.ui.edit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.repo.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBookViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository
) : ViewModel() {

    val book = bookRepository.getBookByIdAsync(savedStateHandle["bookId"] ?: 0)

    fun editBook(editBook: Book) {
        viewModelScope.launch {
            val currentBook = book.value ?: Book()

            currentBook.apply {
                title = editBook.title
                writer = editBook.writer
                publisher = editBook.publisher
            }
            bookRepository.updateBook(currentBook)
        }
    }

}