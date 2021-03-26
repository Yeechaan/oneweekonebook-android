package com.lee.oneweekonebook.ui.search.model

import android.text.Html
import androidx.annotation.Keep
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.utils.convertDateToString
import com.lee.oneweekonebook.utils.convertStringToDate
import com.lee.oneweekonebook.utils.ifEmptyReturnNull
import java.io.Serializable

@Keep
data class SearchBook(
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
) : Serializable

//data class SearchBookResponse(
//    @SerializedName("display")
//    val display: Int,
//    @SerializedName("items")
//    val items: List<Item>,
//    @SerializedName("lastBuildDate")
//    val lastBuildDate: String,
//    @SerializedName("start")
//    val start: Int,
//    @SerializedName("total")
//    val total: Int
//)
//
//data class Item(
//    @SerializedName("author")
//    val author: String,
//    @SerializedName("description")
//    val description: String,
//    @SerializedName("discount")
//    val discount: String,
//    @SerializedName("image")
//    val image: String,
//    @SerializedName("isbn")
//    val isbn: String,
//    @SerializedName("link")
//    val link: String,
//    @SerializedName("price")
//    val price: String,
//    @SerializedName("pubdate")
//    val pubDate: String,
//    @SerializedName("publisher")
//    val publisher: String,
//    @SerializedName("title")
//    val title: String
//)

fun SearchBookResponse.asBookList() = run { item.map { it.asBook() } }

fun Item.asBook() = run {
    SearchBook(
        title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString(),
        writer = Html.fromHtml(author, Html.FROM_HTML_MODE_LEGACY).toString(),
        publisher = Html.fromHtml(publisher, Html.FROM_HTML_MODE_LEGACY).toString(),
        pubDate = Html.fromHtml(pubDate, Html.FROM_HTML_MODE_LEGACY).toString().convertStringToDate("yyyyMMdd").convertDateToString("yyyy-MM-dd"),
        coverImage = coverLargeUrl,
        reviewRank = customerReviewRank,
        reviewCount = reviewCount,
        categoryId = categoryId?.toInt() ?: 0,
        description = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString(),
        link = link,
        price = priceStandard,
    )
}


fun SearchBook.asBook() = run {
    Book(
        title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString(),
        writer = Html.fromHtml(writer, Html.FROM_HTML_MODE_LEGACY).toString(),
        publisher = Html.fromHtml(publisher, Html.FROM_HTML_MODE_LEGACY).toString(),
        subTitle = Html.fromHtml(pubDate, Html.FROM_HTML_MODE_LEGACY).toString(),
        coverImage = coverImage
    )
}