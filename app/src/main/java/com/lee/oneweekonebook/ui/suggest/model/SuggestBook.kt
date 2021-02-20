package com.lee.oneweekonebook.ui.suggest.model

import android.provider.Contacts.SettingsColumns.KEY
import android.text.Html
import com.google.gson.annotations.SerializedName
import com.lee.oneweekonebook.ui.suggest.INTERPARK_KEY
import retrofit2.http.Query

data class SuggestBook(
    var title: String = "",
    var link: String = "",
)


data class SuggestBookResponse(
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
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("sizeheight")
    val sizeHeight: String,
    @SerializedName("sizewidth")
    val sizeWidth: String,
)

fun SuggestBookResponse.asBookList() = run { items.map { it.asBook() } }

fun Item.asBook() = run {
    SuggestBook(
        title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString(),
        link = Html.fromHtml(link, Html.FROM_HTML_MODE_LEGACY).toString()
    )
}