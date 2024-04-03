package com.lee.oneweekonebook.ui.search.model


import com.google.gson.annotations.SerializedName

data class SearchBookResponse(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("item")
    val item: List<Item>,
    @SerializedName("itemsPerPage")
    val itemsPerPage: Int,
    @SerializedName("language")
    val language: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("maxResults")
    val maxResults: Int,
    @SerializedName("pubDate")
    val pubDate: String,
    @SerializedName("query")
    val query: String,
    @SerializedName("queryType")
    val queryType: String,
    @SerializedName("returnCode")
    val returnCode: String,
    @SerializedName("returnMessage")
    val returnMessage: String,
    @SerializedName("searchCategoryId")
    val searchCategoryId: String,
    @SerializedName("searchCategoryName")
    val searchCategoryName: String,
    @SerializedName("startIndex")
    val startIndex: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("totalResults")
    val totalResults: Int
)

data class Item(
    @SerializedName("additionalLink")
    val additionalLink: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("categoryId")
    val categoryId: String?,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("coverLargeUrl")
    val coverLargeUrl: String,
    @SerializedName("coverSmallUrl")
    val coverSmallUrl: String,
    @SerializedName("customerReviewRank")
    val customerReviewRank: Double,
    @SerializedName("description")
    val description: String,
    @SerializedName("discountRate")
    val discountRate: String,
    @SerializedName("isbn")
    val isbn: String?,
    @SerializedName("itemId")
    val itemId: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("mileage")
    val mileage: String,
    @SerializedName("mileageRate")
    val mileageRate: String,
    @SerializedName("mobileLink")
    val mobileLink: String,
    @SerializedName("priceSales")
    val priceSales: Int,
    @SerializedName("priceStandard")
    val priceStandard: Int,
    @SerializedName("pubDate")
    val pubDate: String,
    @SerializedName("publisher")
    val publisher: String,
    @SerializedName("reviewCount")
    val reviewCount: Int,
    @SerializedName("saleStatus")
    val saleStatus: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("translator")
    val translator: String
)