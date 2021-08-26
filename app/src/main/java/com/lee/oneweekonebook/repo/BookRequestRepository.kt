package com.lee.oneweekonebook.repo

import com.lee.oneweekonebook.di.BookApiQualifier
import com.lee.oneweekonebook.network.BookApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRequestRepository @Inject constructor(
    @BookApiQualifier private val bookApiService: BookApiService
) {

    suspend fun searchBook(
        query: String
    ) = withContext(Dispatchers.IO) {
        bookApiService.searchBook(query = query)
    }

    suspend fun getRecommendationBook(
        categoryId: Int
    ) = withContext(Dispatchers.IO) {
        bookApiService.getSuggestBookAsync(categoryId = categoryId)
    }
}