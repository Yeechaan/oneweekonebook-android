package com.lee.oneweekonebook.repo

import com.lee.oneweekonebook.common.Result
import com.lee.oneweekonebook.di.BookApiQualifier
import com.lee.oneweekonebook.di.IoDispatcher
import com.lee.oneweekonebook.network.BookApiService
import com.lee.oneweekonebook.network.RESPONSE_CODE_SUCCESS
import com.lee.oneweekonebook.ui.search.model.SearchBookResponse
import com.lee.oneweekonebook.ui.suggest.model.RecommendBookResponse
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRequestRepository @Inject constructor(
    @BookApiQualifier private val bookApiService: BookApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun searchBook(
        query: String
    ): Result<SearchBookResponse> = withContext(ioDispatcher) {
        try {
            val response = bookApiService.searchBook(query = query)
            val returnCode = response.returnCode

            if (returnCode == RESPONSE_CODE_SUCCESS) {
                Result.Success(response)
            } else {
                Logger.i("Api Issue searchBook : $returnCode")
                Result.Error(returnCode)
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }


    suspend fun searchBookByISBN(
        isbn: String
    ): Result<SearchBookResponse> = withContext(ioDispatcher) {
        try {
            val response = bookApiService.searchBookByISBN(query = isbn)
            val returnCode = response.returnCode
            if (returnCode == RESPONSE_CODE_SUCCESS) {
                return@withContext Result.Success(response)
            } else {
                Logger.i("Api Issue searchBookByISBN : $returnCode")
                return@withContext Result.Error(returnCode)
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.toString())
        }
    }


    suspend fun getRecommendationBook(
        categoryId: Int
    ): Result<RecommendBookResponse> = withContext(ioDispatcher) {
        try {
            val response = bookApiService.getSuggestBook(categoryId = categoryId)

            Logger.d(response)

            val returnCode = response.returnCode
            if (returnCode == RESPONSE_CODE_SUCCESS) {
                return@withContext Result.Success(response)
            } else {
                Logger.i("Api Issue getSuggestBook : $returnCode")
                return@withContext Result.Error(returnCode)
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.toString())
        }
    }

}