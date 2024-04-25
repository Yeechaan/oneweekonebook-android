package com.lee.oneweekonebook.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    bokRepository: BookRepository,
) : ViewModel() {

    val books = bokRepository.getBooks(BOOK_TYPE_READING).asLiveData()

    val isBookEmpty = books.map {
        it.isEmpty()
    }
}