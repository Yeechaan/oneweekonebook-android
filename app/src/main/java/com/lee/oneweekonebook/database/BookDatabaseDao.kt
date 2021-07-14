package com.lee.oneweekonebook.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lee.oneweekonebook.database.model.Book

@Dao
interface BookDatabaseDao {

    @Insert
    fun insert(book: Book)

    @Update
    fun update(book: Book)

    @Query("SELECT * FROM book_history_table WHERE id = :id")
    fun getBookAsync(id: Int): LiveData<Book>

    @Query("SELECT * FROM book_history_table WHERE id = :id")
    fun getBook(id: Int): Book

    @Query("SELECT * FROM book_history_table WHERE type = :type")
    fun getBooksByType(type: Int): LiveData<List<Book>>

    @Query("SELECT * FROM book_history_table WHERE type = :type")
    fun getBooksType(type: Int): List<Book>

    @Query("SELECT * FROM book_history_table")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("DELETE FROM book_history_table where id = :id")
    fun deleteBook(id: Int)

    @Query("SELECT * FROM book_history_table WHERE title LIKE :title LIMIT 1")
    fun getBookWithTitle(title: String): Book?

    @Query("DELETE FROM book_history_table")
    fun clear()
}
