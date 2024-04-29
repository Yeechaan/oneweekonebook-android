package com.lee.oneweekonebook.repository

import com.lee.oneweekonebook.datasource.local.BookLocalDataSource
import com.lee.oneweekonebook.datasource.remote.BookRemoteDataSource
import com.lee.oneweekonebook.mapper.BookDomain
import com.lee.oneweekonebook.mapper.asData
import com.lee.oneweekonebook.mapper.asDomain
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBook
import com.lee.oneweekonebook.ui.search.model.asBookList
import com.lee.oneweekonebook.ui.suggest.model.asBookList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookRemoteDataSource: BookRemoteDataSource,
    private val bookLocalDataSource: BookLocalDataSource,
) : BookRepository {

    override suspend fun searchBookByTitle(query: String): Result<List<BookInfo>> {
        val result = bookRemoteDataSource.searchBook(query)
        return result.fold(
            onSuccess = {
                Result.success(it.asBookList())
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun searchBookByISBN(isbn: String): Result<BookInfo> {
        val result = bookRemoteDataSource.searchBookByISBN(isbn)
        return result.fold(
            onSuccess = {
                it.item.firstOrNull()?.let {
                    Result.success(it.asBook())
                } ?: Result.failure(Exception("No Book Matched with isbn."))
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun searchRecommendationBooks(categoryId: Int): Result<List<BookInfo>> {
        val result = bookRemoteDataSource.getRecommendationBook(categoryId)
        return result.fold(
            onSuccess = {
                Result.success(it.asBookList())
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }


    override suspend fun saveBook(bookInfo: BookInfo) {
        val book = bookInfo.asBook()
        bookLocalDataSource.insertBook(book)
    }

    override suspend fun updateBook(book: BookDomain) {
        val updatedBook = book.asData()
        bookLocalDataSource.updateBook(updatedBook)
    }

    override suspend fun updateBookType(id: Int, type: Int) {
        val book = bookLocalDataSource.getBook(id).apply {
            this.type = type
        }
        bookLocalDataSource.updateBook(book)
    }

    override suspend fun deleteBook(id: Int) {
        bookLocalDataSource.deleteBook(id)
    }

    override fun getBook(id: Int): Flow<BookDomain> {
        return bookLocalDataSource.getBookAsFlow(id).map { it.asDomain() }
    }

    override fun getBooks(type: Int): Flow<List<BookDomain>> {
        return bookLocalDataSource.getBooksAsFlow(type).map { it.asDomain() }
    }

    override suspend fun isSameBookSaved(isbn: String): Boolean {
        return bookLocalDataSource.isSameBookSaved(isbn)
    }
}