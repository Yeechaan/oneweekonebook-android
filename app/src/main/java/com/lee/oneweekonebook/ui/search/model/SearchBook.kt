package com.lee.oneweekonebook.ui.search.model

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
            title = title,
            writer = author,
            publisher = publisher,
            pubDate = pubDate,
            coverImage = image
    )
}