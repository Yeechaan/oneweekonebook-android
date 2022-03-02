package com.lee.oneweekonebook.repo

import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepository @Inject constructor(
    bookDatabase: BookDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val bookDatabaseDao = bookDatabase.bookDatabaseDao

    suspend fun addBook(book: Book) = withContext(ioDispatcher) { bookDatabaseDao.insert(book) }

    suspend fun updateBook(book: Book) = withContext(ioDispatcher) { bookDatabaseDao.update(book) }

    fun getBookByIdAsync(id: Int) = bookDatabaseDao.getBookAsync(id)

    suspend fun getBookById(id: Int) = withContext(ioDispatcher) { bookDatabaseDao.getBook(id) }

    fun getAllBookByTypeAsync(type: Int) = bookDatabaseDao.getBooksByTypeAsync(type)

    suspend fun deleteBookById(id: Int) = withContext(ioDispatcher) { bookDatabaseDao.deleteBook(id) }

    suspend fun isSameBookSaved(title: String) = withContext(ioDispatcher) {
        bookDatabaseDao.getBookByTitle(title)?.let { true } ?: false
    }
}