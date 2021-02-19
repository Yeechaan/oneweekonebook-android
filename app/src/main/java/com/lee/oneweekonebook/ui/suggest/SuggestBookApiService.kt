package com.lee.oneweekonebook.ui.suggest

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lee.oneweekonebook.ui.suggest.model.RecommendBookResponse
import com.lee.oneweekonebook.ui.suggest.model.SuggestBookResponse
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL_INTERPARK = "http://book.interpark.com/"
const val INTERPARK_KEY = "8892D72AADCAC82157036D312CA3FCF0F5BA6ED181C8404722D7D4418F1BDD2E"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL_INTERPARK)
    .build()

interface SuggestBookApiService {

    @GET("api/recommend.api")
    fun getSuggestBookAsync(@Query("key") key: String = INTERPARK_KEY, @Query("categoryId") categoryId: Int, @Query("output") output: String = "json"): Deferred<RecommendBookResponse>

}

object SuggestBookApi {
    val suggestBookApiService: SuggestBookApiService by lazy {
        retrofit.create(SuggestBookApiService::class.java)
    }
}