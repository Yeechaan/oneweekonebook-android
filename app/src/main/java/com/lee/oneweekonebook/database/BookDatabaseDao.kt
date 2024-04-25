package com.lee.oneweekonebook.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lee.oneweekonebook.database.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: Book)

    @Update
    fun update(book: Book)

    @Query("SELECT * FROM book_history_table WHERE id = :id")
    fun getBook(id: Int): Book

    @Query("SELECT * FROM book_history_table WHERE id = :id")
    fun getBookAsFlow(id: Int): Flow<Book>

    @Query("SELECT * FROM book_history_table WHERE type = :type")
    fun getBookListAsFlow(type: Int): Flow<List<Book>>

    @Query("DELETE FROM book_history_table where id = :id")
    fun deleteBook(id: Int)

    @Query("SELECT * FROM book_history_table WHERE title LIKE :isbn LIMIT 1")
    fun getBookByIsbn(isbn: String): Book?
}
