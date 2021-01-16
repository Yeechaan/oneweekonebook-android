package com.lee.oneweekonebook.database.model

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "book_history_table")
data class Book(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        @ColumnInfo(name = "title")
        var title: String = "",
        @ColumnInfo(name = "writer")
        var writer: String = "",
        @ColumnInfo(name = "publisher")
        var publisher: String = "",
        @ColumnInfo(name = "coverImage")
        var coverImage: String = "",
        @ColumnInfo(name = "subTitle")
        var subTitle: String = "",
        @ColumnInfo(name = "page")
        var page: String = "",
        @ColumnInfo(name = "contents")
        var contents: String = "",
        @ColumnInfo(name = "review")
        var review: String = "",
        @ColumnInfo(name = "startDate")
        var startDate: Long? = 0L,
        @ColumnInfo(name = "endDate")
        var endDate: Long? = 0L,
        @ColumnInfo(name = "type")
        var type: Int = BOOK_TYPE_UNKNOWN,
)

const val BOOK_TYPE_UNKNOWN = -1
const val BOOK_TYPE_WISH = 0
const val BOOK_TYPE_READING = 1
const val BOOK_TYPE_DONE = 2

@Target(AnnotationTarget.TYPE)
@IntDef(
    BOOK_TYPE_UNKNOWN,
    BOOK_TYPE_WISH,
    BOOK_TYPE_READING,
    BOOK_TYPE_DONE
)
annotation class BookType