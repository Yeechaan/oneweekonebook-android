package com.lee.oneweekonebook.ui.edit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBookViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val book = bookRepository.getBook(savedStateHandle["bookId"] ?: 0).asLiveData()

    fun editBook() {
        viewModelScope.launch {
            book.value?.let {
                bookRepository.updateBook(it)
            }
        }
    }

}