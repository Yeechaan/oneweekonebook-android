package com.lee.oneweekonebook.network

import com.lee.oneweekonebook.ui.search.model.SearchBookResponse
import com.lee.oneweekonebook.ui.suggest.model.RecommendBookResponse
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL_INTERPARK = "http://book.interpark.com/"
const val INTERPARK_KEY = "8892D72AADCAC82157036D312CA3FCF0F5BA6ED181C8404722D7D4418F1BDD2E"
const val OUTPUT_TYPE = "json"
const val MAX_RESULT = 100

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL_INTERPARK)
    .build()

interface BookApiService {

    @GET("api/search.api")
    suspend fun searchBookAsync(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("query") query: String,
        @Query("output") output: String = OUTPUT_TYPE,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
    ): SearchBookResponse


    @GET("api/recommend.api")
    suspend fun getSuggestBookAsync(
        @Query("key") key: String = INTERPARK_KEY,
        @Query("categoryId") categoryId: Int,
        @Query("output") output: String = OUTPUT_TYPE,
    ): RecommendBookResponse

}

object BookApi {
    val bookApiService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}