package com.lee.oneweekonebook.ui.suggest.model


import com.google.gson.annotations.SerializedName

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
    val categoryId: String,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("coverLargeUrl")
    val coverLargeUrl: String,
    @SerializedName("coverSmallUrl")
    val coverSmallUrl: String,
    @SerializedName("customerReviewRank")
    val customerReviewRank: Int,
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

fun RecommendBook.getRandomCategory() = (categoryGlobalBook.keys + categoryGlobalBook.keys).random()
