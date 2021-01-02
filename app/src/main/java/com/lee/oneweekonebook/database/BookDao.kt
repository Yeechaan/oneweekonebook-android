package com.lee.oneweekonebook.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lee.oneweekonebook.database.model.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE id IN (:id)")
    fun loadAllByIds(id: IntArray): List<Book>

    @Query("SELECT * FROM book WHERE title LIKE :first AND " +
            "title LIKE :last LIMIT 1")
    fun findByTitle(first: String, last: String): Book

    @Insert
    fun insertAll(vararg books: Book)

    @Delete
    fun delete(book: Book)
}
