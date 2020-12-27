package com.lee.oneweekonebook.ui.search

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lee.oneweekonebook.ui.search.model.SearchBookResponse
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val BASE_URL = "https://openapi.naver.com/"

private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

interface SearchBookApiService {

    @Headers(
            "X-Naver-Client-Id: _44pqpd5AW8yjqYMzX52",
            "X-Naver-Client-Secret: ocLrfBgqLF"
    )
    @GET("v1/search/book.json")
    fun getSearchBookAsync(@Query("query") query: String): Deferred<SearchBookResponse>
}

object SearchBookApi {
    val searchBookApiService: SearchBookApiService by lazy {
        retrofit.create(SearchBookApiService::class.java)
    }
}