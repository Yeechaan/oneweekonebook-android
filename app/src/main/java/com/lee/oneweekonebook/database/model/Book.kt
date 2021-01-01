package com.lee.oneweekonebook.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Book(
        @PrimaryKey
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
        var startDate: Date = Date(),
        @ColumnInfo(name = "endDate")
        var endDate: Date = Date(),
)
