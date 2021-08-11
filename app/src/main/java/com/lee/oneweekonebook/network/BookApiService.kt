package com.lee.oneweekonebook.network

import com.lee.oneweekonebook.BuildConfig
import com.lee.oneweekonebook.ui.search.model.SearchBookResponse
import com.lee.oneweekonebook.ui.suggest.model.RecommendBookResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Qualifier


private const val BASE_URL_INTERPARK = "http://book.interpark.com/"
const val INTERPARK_KEY = BuildConfig.BOOK_API_KEY
const val OUTPUT_TYPE = "json"
const val MAX_RESULT = 100


interface BookApiService {
    @GET("api/search.api")
    suspend fun searchBook(
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


@Module
@InstallIn(SingletonComponent::class)
class BookRetrofitModule {

    @BookApiQualifier
    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL_INTERPARK)
        .build()
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class BookApiQualifier