package com.lee.oneweekonebook.mapper

import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.Book

data class BookDomain(
    val id: Int = 0,
    var title: String = "",
    var writer: String = "",
    var publisher: String = "",
    val pubDate: String = "",
    val coverImage: String = "",
    val subTitle: String = "",
    val page: String = "",
    var contents: String = "",
    var review: String = "",
    val startDate: Long = System.currentTimeMillis(),
    var endDate: Long = startDate,
    var type: Int = BOOK_TYPE_UNKNOWN,
    val isbn: String = "",
)

fun List<Book>.asDomain() = this.map { it.asDomain() }

fun Book.asDomain() = run {
    BookDomain(
        id = id,
        title = title,
        writer = writer,
        publisher = publisher,
        pubDate = pubDate,
        coverImage = coverImage,
        subTitle = subTitle,
        page = page,
        contents = contents,
        review = review,
        startDate = startDate,
        endDate = endDate,
        type = type,
        isbn = isbn
    )
}

fun BookDomain.asData() = run {
    Book(
        id = id,
        title = title,
        writer = writer,
        publisher = publisher,
        pubDate = pubDate,
        coverImage = coverImage,
        subTitle = subTitle,
        page = page,
        contents = contents,
        review = review,
        startDate = startDate,
        endDate = endDate,
        type = type,
        isbn = isbn
    )
}