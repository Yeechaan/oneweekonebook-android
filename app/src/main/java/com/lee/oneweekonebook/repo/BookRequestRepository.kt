package com.lee.oneweekonebook.repo

import com.lee.oneweekonebook.network.BookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRequestRepository @Inject constructor(){

    suspend fun searchBook(
        query: String
    ) = withContext(Dispatchers.IO) {
        BookApi.bookApiService.searchBook(query = query)
    }

    suspend fun getRecommendationBook(
        categoryId: Int
    ) = withContext(Dispatchers.IO) {
        BookApi.bookApiService.getSuggestBookAsync(categoryId = categoryId)
    }
}