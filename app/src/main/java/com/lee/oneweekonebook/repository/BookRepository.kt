package com.lee.oneweekonebook.repository

import com.lee.oneweekonebook.mapper.BookDomain
import com.lee.oneweekonebook.ui.search.model.BookInfo
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBookByTitle(query: String): Result<List<BookInfo>>
    suspend fun searchBookByISBN(isbn: String): Result<BookInfo>
    suspend fun searchRecommendationBooks(categoryId: Int): Result<List<BookInfo>>

    suspend fun saveBook(bookInfo: BookInfo)
    suspend fun updateBook(book: BookDomain)
    suspend fun updateBookType(id: Int, type: Int)
    suspend fun deleteBook(id: Int)
    fun getBook(id: Int): Flow<BookDomain>
    fun getBooks(type: Int): Flow<List<BookDomain>>

    suspend fun isSameBookSaved(isbn: String): Boolean
}