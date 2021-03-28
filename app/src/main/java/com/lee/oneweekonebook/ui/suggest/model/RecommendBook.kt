package com.lee.oneweekonebook.ui.suggest.model


import android.text.Html
import com.google.gson.annotations.SerializedName
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.utils.convertDateToString
import com.lee.oneweekonebook.utils.convertStringToDate

data class RecommendBook(
    var title: String = "",
    var link: String = "",
)

data class RecommendBookResponse(
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("item")     //
    val item: List<ItemRecommend>,
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
    @SerializedName("queryType")
    val queryType: String,
    @SerializedName("returnCode")       //
    val returnCode: String,
    @SerializedName("returnMessage")        //
    val returnMessage: String,
    @SerializedName("searchCategoryId")     //
    val searchCategoryId: String,
    @SerializedName("searchCategoryName")       //
    val searchCategoryName: String,
    @SerializedName("startIndex")
    val startIndex: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("totalResults")     //
    val totalResults: Int
)

data class ItemRecommend(
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
    val isbn: String,
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

fun RecommendBookResponse.asBookList() = run { item.map { it.asBook() } }

fun ItemRecommend.asBook() = run {
    BookInfo(
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

val categoryLocalBook = hashMapOf(
    100 to "국내도서",
    101 to "소설",
    102 to "시/에세이",
    103 to "예술/대중문화",
    104 to "사회과학",
    105 to "역사와 문화",
    107 to "잡지",
    108 to "만화",
    109 to "유아",
    110 to "아동",
    111 to "가정과 생활",
    112 to "청소년",
    113 to "초등학습서",
    114 to "고등학습서",
    115 to "국어/외국어/사전",
    116 to "자연과 과학",
    117 to "경제경영",
    118 to "자기계발",
    119 to "인문",
    120 to "종교/역학",
    122 to "컴퓨터/인터넷",
    123 to "자격서/수험서",
    124 to "취미/레져",
    125 to "전공도서/대학교재",
    126 to "건강/뷰티",
    128 to "여행",
    129 to "중등학습서",
)

val categoryGlobalBook = hashMapOf(
    201 to "어린이",
    203 to "ELT/사전",
    205 to "문학",
    206 to "경영/인문",
    207 to "예술/디자인",
    208 to "실용",
    209 to "해외잡지",
    210 to "대학교재/전문서적",
    211 to "컴퓨터",
    214 to "일본도서",
    215 to "프랑스도서",
    216 to "중국도서",
    217 to "해외주문원서",
)

fun RecommendBook.getRandomCategory() = (categoryLocalBook.keys + categoryGlobalBook.keys).random()
