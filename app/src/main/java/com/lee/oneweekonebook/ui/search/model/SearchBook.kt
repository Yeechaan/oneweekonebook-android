package com.lee.oneweekonebook.ui.search.model

import android.text.Html
import androidx.annotation.Keep
import com.lee.oneweekonebook.database.model.BOOK_TYPE_UNKNOWN
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.utils.convertDateToString
import com.lee.oneweekonebook.utils.convertStringToDate
import java.io.Serializable

@Keep
data class BookInfo(
    val id: Int = -1,
    var title: String = "",
    var writer: String = "",
    var publisher: String = "",
    var pubDate: String = "",
    var coverImage: String = "",
    var reviewRank: Double = 0.0,
    var reviewCount: Int = 0,
    var categoryId: Int? = 0,
    var description: String = "",
    var link: String = "",
    var price: Int = 0,
    var isbn: String = "",
    var type: Int = BOOK_TYPE_UNKNOWN,
    var startDate: Long = 0,
    var endDate: Long = 0,
) : Serializable


fun SearchBookResponse.asBookList() = run { item.map { it.asBook() } }

fun Item.asBook() = run {
    BookInfo(
        title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString(),
        writer = Html.fromHtml(author, Html.FROM_HTML_MODE_LEGACY).toString(),
        publisher = Html.fromHtml(publisher, Html.FROM_HTML_MODE_LEGACY).toString(),
        pubDate = Html.fromHtml(pubDate, Html.FROM_HTML_MODE_LEGACY).toString().convertStringToDate("yyyyMMdd")
            .convertDateToString("yyyy-MM-dd"),
        coverImage = coverLargeUrl,
        reviewRank = customerReviewRank,
        reviewCount = reviewCount,
        categoryId = categoryId?.toInt() ?: 0,
        description = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString(),
        link = link,
        price = priceStandard,
        isbn = isbn ?: "",
    )
}


fun BookInfo.asBook() = run {
    Book(
        title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString(),
        writer = Html.fromHtml(writer, Html.FROM_HTML_MODE_LEGACY).toString(),
        publisher = Html.fromHtml(publisher, Html.FROM_HTML_MODE_LEGACY).toString(),
        coverImage = coverImage,
        pubDate = Html.fromHtml(pubDate, Html.FROM_HTML_MODE_LEGACY).toString(),
        isbn = isbn
    )
}

fun BookInfo.asData() = run {
    Book(
        id = 0,
        title = "",
        writer = "",
        publisher = "",
        pubDate = "",
        coverImage = "",
        subTitle = "",
        page = "",
        contents = "",
        review = "",
        startDate = 0,
        endDate = 0,
        type = 0,
        isbn = ""
    )
}