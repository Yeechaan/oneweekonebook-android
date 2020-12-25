package com.lee.oneweekonebook.ui.search.model

import com.google.gson.annotations.SerializedName

data class SearchBookRequest(
        @SerializedName("query")
        var query: String = "",
)


data class SearchBookResponse(
        @SerializedName("detail")
        var detail: String = "",
)