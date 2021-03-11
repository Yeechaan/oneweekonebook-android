package com.lee.oneweekonebook.ui.search.model

import android.text.Html
import com.google.gson.annotations.SerializedName

data class SearchBook(
    var title: String = "",
    var writer: String = "",
    var publisher: String = "",
    var pubDate: String = "",
    var coverImage: String = "",
)

data class SearchBookResponse(
    @SerializedName("display")
    val display: Int,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("lastBuildDate")
    val lastBuildDate: String,
    @SerializedName("start")
    val start: Int,
    @SerializedName("total")
    val total: Int
)

data class Item(
    @SerializedName("author")
    val author: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("isbn")
    val isbn: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("pubdate")
    val pubDate: String,
    @SerializedName("publisher")
    val publisher: String,
    @SerializedName("title")
    val title: String
)

fun SearchBookResponse.asBookList() = run { items.map { it.asBook() } }

fun Item.asBook() = run {
    SearchBook(
        title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString(),
        writer = Html.fromHtml(author, Html.FROM_HTML_MODE_LEGACY).toString(),
        publisher = Html.fromHtml(publisher, Html.FROM_HTML_MODE_LEGACY).toString(),
        pubDate = Html.fromHtml(pubDate, Html.FROM_HTML_MODE_LEGACY).toString(),
        coverImage = image
    )
}