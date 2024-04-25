package com.lee.oneweekonebook.di

import com.lee.oneweekonebook.repository.BookRepository
import com.lee.oneweekonebook.repository.BookRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBookRepository(bookRepositoryImpl: BookRepositoryImpl): BookRepository
}