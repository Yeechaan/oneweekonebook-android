package com.lee.oneweekonebook.datasource.remote

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

class BookRemoteDataSource @Inject constructor(
    @BookApiQualifier private val bookApiService: BookApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun searchBook(query: String): Result<SearchBookResponse> = withContext(ioDispatcher) {
        try {
            val response = bookApiService.searchBook(query = query)
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val returnCode = body.returnCode
                if (returnCode == RESPONSE_CODE_SUCCESS) {
                    Result.success(body)
                } else {
                    Logger.i("Api Issue searchBookByISBN : $returnCode")
                    Result.failure(Exception(returnCode))
                }
            } else {
                // 응답 코드가 [200..300)에 포함되지 않는 경우
                val responseCode = response.code()
                Logger.i("Network Issue : $responseCode")
                Result.failure(Exception(responseCode.toString()))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toString()))
        }
    }


    suspend fun searchBookByISBN(isbn: String): Result<SearchBookResponse> = withContext(ioDispatcher) {
        try {
            val response = bookApiService.searchBookByISBN(query = isbn)
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val returnCode = body.returnCode
                if (returnCode == RESPONSE_CODE_SUCCESS) {
                    Result.success(body)
                } else {
                    Logger.i("Api Issue searchBookByISBN : $returnCode")
                    Result.failure(Exception(returnCode))
                }
            } else {
                // 응답 코드가 [200..300)에 포함되지 않는 경우
                val responseCode = response.code()
                Logger.i("Network Issue : $responseCode")
                Result.failure(Exception(responseCode.toString()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getRecommendationBook(categoryId: Int): Result<RecommendBookResponse> = withContext(ioDispatcher) {
        try {
            val response = bookApiService.getSuggestBook(categoryId = categoryId)
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val returnCode = body.returnCode
                if (returnCode == RESPONSE_CODE_SUCCESS) {
                    Result.success(body)
                } else {
                    Logger.i("Api Issue getSuggestBook : $returnCode")
                    Result.failure(Exception(returnCode))
                }
            } else {
                // 응답 코드가 [200..300)에 포함되지 않는 경우
                val responseCode = response.code()
                Logger.i("Network Issue : $responseCode")
                Result.failure(Exception(responseCode.toString()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}