package com.lee.oneweekonebook.di

import com.lee.oneweekonebook.network.BookApiQualifier
import com.lee.oneweekonebook.network.BookApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class BookApiModule {

    @BookApiQualifier
    @Provides
    fun bookApiService(@BookApiQualifier api: Retrofit): BookApiService {
        return api.create(BookApiService::class.java)
    }
}