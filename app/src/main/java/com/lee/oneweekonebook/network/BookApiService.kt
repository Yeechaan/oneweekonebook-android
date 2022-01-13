package com.lee.oneweekonebook.network

import com.lee.oneweekonebook.BuildConfig
import com.lee.oneweekonebook.ui.search.model.SearchBookResponse
import com.lee.oneweekonebook.ui.suggest.model.RecommendBookResponse
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL_INTERPARK = "http://book.interpark.com/"
const val INTERPARK_KEY = BuildConfig.BOOK_API_KEY
const val OUTPUT_TYPE = "json"
const val QUERY_TYPE = "isbn"
const val MAX_RESULT = 100

interface BookApiService {
    @GET("api/search.api")
    suspend fun searchBook(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("query") query: String,
        @Query("output") output: String = OUTPUT_TYPE,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
    ): SearchBookResponse

    @GET("api/search.api")
    suspend fun searchBookByISBN(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("query") query: String,
        @Query("output") output: String = OUTPUT_TYPE,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
        @Query("queryType") queryType: String = QUERY_TYPE,
    ): SearchBookResponse

    @GET("api/recommend.api")
    suspend fun getSuggestBookAsync(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("categoryId") categoryId: Int,
        @Query("output") output: String = OUTPUT_TYPE,
    ): RecommendBookResponse
}