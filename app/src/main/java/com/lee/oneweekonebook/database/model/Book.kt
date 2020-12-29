package com.lee.oneweekonebook.database.model

import java.util.*

data class Book(
        var id: Int = 0,
        var title: String = "",
        var writer: String = "",
        var publisher: String = "",
        var coverImage: String = "",
        var subTitle: String = "",
        var page: String = "",
        var contents: String = "",
        var review: String = "",
        var startDate: Date = Date(),
        var endDate: Date = Date(),
)
