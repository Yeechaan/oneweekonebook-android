package com.lee.oneweekonebook.ui.home.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.repo.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    bookRepository: BookRepository
) : ViewModel() {

    val books = bookRepository.getAllBookByTypeAsync(BOOK_TYPE_READING)

    val isBookEmpty = Transformations.map(books) {
        it.isNullOrEmpty()
    }

}