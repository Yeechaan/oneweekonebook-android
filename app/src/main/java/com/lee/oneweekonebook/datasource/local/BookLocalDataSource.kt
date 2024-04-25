package com.lee.oneweekonebook.datasource.local

import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookLocalDataSource @Inject constructor(
    bookDatabase: BookDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    private val bookDatabaseDao = bookDatabase.bookDatabaseDao

    suspend fun getBook(id: Int) = withContext(ioDispatcher) { bookDatabaseDao.getBook(id) }
    fun getBookAsFlow(id: Int) = bookDatabaseDao.getBookAsFlow(id)
    fun getBooksAsFlow(type: Int) = bookDatabaseDao.getBookListAsFlow(type)

    suspend fun insertBook(book: Book) = withContext(ioDispatcher) { bookDatabaseDao.insert(book) }
    suspend fun updateBook(book: Book) = withContext(ioDispatcher) { bookDatabaseDao.update(book) }
    suspend fun deleteBook(id: Int) = withContext(ioDispatcher) { bookDatabaseDao.deleteBook(id) }

    suspend fun isSameBookSaved(isbn: String) = withContext(ioDispatcher) {
        bookDatabaseDao.getBookByIsbn(isbn)?.let { true } ?: false
    }
}