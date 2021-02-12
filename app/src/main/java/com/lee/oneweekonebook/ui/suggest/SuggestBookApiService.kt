package com.lee.oneweekonebook.ui.suggest

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lee.oneweekonebook.ui.suggest.model.SuggestBookResponse
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL_INTERPARK = "http://book.interpark.com/api/recommend.api/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL_INTERPARK)
    .build()

interface SuggestBookApiService {

    @GET("api/bestSeller.api")
    fun getSuggestBookAsync(@Query("key") key: String, @Query("category") category: Int, @Query("output") output: String = "json"): Deferred<SuggestBookResponse>

}

object SuggestBookApi {
    val suggestBookApiService: SuggestBookApiService by lazy {
        retrofit.create(SuggestBookApiService::class.java)
    }
}