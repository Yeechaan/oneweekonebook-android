package com.lee.oneweekonebook.network

import com.lee.oneweekonebook.BuildConfig
import com.lee.oneweekonebook.ui.search.model.SearchBookResponse
import com.lee.oneweekonebook.ui.suggest.model.RecommendBookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL_INTERPARK = "http://book.interpark.com/"
const val INTERPARK_KEY = BuildConfig.BOOK_API_KEY
const val OUTPUT_TYPE = "json"
const val QUERY_TYPE = "isbn"
const val MAX_RESULT = 100
const val RESPONSE_CODE_SUCCESS = "000"

/**
 * 인터파크 API를 사용하여 서버와 통신하는 메서드 정의
 * 책검색 api 문서 : https://book.interpark.com/bookPark/html/bookpinion/api_booksearch.html
 * 추천도서 api 문서 : https://book.interpark.com/bookPark/html/bookpinion/api_recommend.html
 * */

interface BookApiService {
    @GET("api/search.api")
    suspend fun searchBook(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("query") query: String,
        @Query("output") output: String = OUTPUT_TYPE,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
    ): Response<SearchBookResponse>

    @GET("api/search.api")
    suspend fun searchBookByISBN(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("query") query: String,
        @Query("output") output: String = OUTPUT_TYPE,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
        @Query("queryType") queryType: String = QUERY_TYPE,
    ): Response<SearchBookResponse>

    @GET("api/recommend.api")
    suspend fun getSuggestBook(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("categoryId") categoryId: Int,
        @Query("output") output: String = OUTPUT_TYPE,
    ): Response<RecommendBookResponse>
}