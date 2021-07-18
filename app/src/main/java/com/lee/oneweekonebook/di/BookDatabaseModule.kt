package com.lee.oneweekonebook.di

import android.content.Context
import androidx.room.Room
import com.lee.oneweekonebook.database.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BookDatabaseModule {

    @Provides
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase =
        Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "book_history_database"
        ).build()

}