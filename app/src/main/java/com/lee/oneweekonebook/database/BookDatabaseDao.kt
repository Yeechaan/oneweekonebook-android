package com.lee.oneweekonebook.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lee.oneweekonebook.database.model.Book

@Dao
interface BookDatabaseDao {

    @Insert
    fun insert(book: Book)

    @Update
    fun update(book: Book)

    @Query("SELECT * FROM book_history_table WHERE id = :id")
    fun get(id: Long): Book?

    @Query("SELECT * FROM book_history_table")
    fun getAllBooks(): LiveData<List<Book>>

    @Delete
    fun delete(book: Book)

    @Query("DELETE FROM book_history_table")
    fun clear()

    @Query("SELECT * FROM book_history_table WHERE title LIKE :title LIMIT 1")
    fun getBookWithTitle(title: String): Book
}
