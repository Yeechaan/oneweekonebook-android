package com.lee.oneweekonebook.repo

import com.lee.oneweekonebook.database.BookDatabase
import com.lee.oneweekonebook.database.model.Book
import javax.inject.Inject

class BookRepository @Inject constructor(
    bookDatabase: BookDatabase
) {

    private val bookDatabaseDao = bookDatabase.bookDatabaseDao

    fun addBook(book: Book) = bookDatabaseDao.insert(book)


    fun updateBook(book: Book) = bookDatabaseDao.update(book)


    fun getBookByIdAsync(id: Int) = bookDatabaseDao.getBookAsync(id)


    fun getBookById(id: Int) = bookDatabaseDao.getBook(id)


    fun getAllBookByTypeAsync(type: Int) = bookDatabaseDao.getBooksByTypeAsync(type)


    fun deleteBookById(id: Int) = bookDatabaseDao.deleteBook(id)


    fun isSameBookSaved(title: String) =
        bookDatabaseDao.getBookByTitle(title)?.let {
            true
        } ?: false

}