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

    @Query("SELECT * FROM book WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Book>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Book

    @Insert
    fun insertAll(vararg users: Book)

    @Delete
    fun delete(user: Book)
}
