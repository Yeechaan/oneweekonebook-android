package com.lee.oneweekonebook.di

import com.lee.oneweekonebook.network.BASE_URL_INTERPARK
import com.lee.oneweekonebook.network.BookApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier


@Module
@InstallIn(SingletonComponent::class)
class BookApiModule {

    @BookApiQualifier
    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL_INTERPARK)
        .build()

    @BookApiQualifier
    @Provides
    fun bookApiService(@BookApiQualifier api: Retrofit): BookApiService {
        return api.create(BookApiService::class.java)
    }
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class BookApiQualifier