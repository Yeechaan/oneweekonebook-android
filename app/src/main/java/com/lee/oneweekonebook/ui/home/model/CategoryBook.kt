package com.lee.oneweekonebook.ui.home.model

import com.lee.oneweekonebook.R

data class CategoryBook(
    var icon: Int = 0,
    var title: String = "",
    var type: Int = 0
)


val categoryBook = hashMapOf(
    100 to "국내도서",
    101 to "소설",
    102 to "시/에세이",
    103 to "예술",
    104 to "사회/과학",
    105 to "역사/문화",
    107 to "잡지",
    108 to "만화",
    109 to "유아",
    110 to "아동",
    111 to "가정/생활",
    112 to "청소년",
    113 to "초등학습서",
    114 to "고등학습서",
    115 to "국어/\n외국어/\n사전",
    116 to "자연/과학",
    117 to "경제/경영",
    118 to "자기계발",
    119 to "인문",
    120 to "종교",
    122 to "컴퓨터",
    123 to "자격서/\n수험서",
    124 to "취미/레져",
    125 to "전공도서/\n대학교재",
    126 to "건강/뷰티",
    128 to "여행",
    129 to "중등학습서",
)

val categoryBooks = arrayListOf(
    CategoryBook(icon = R.drawable.ic_novel, title = categoryBook.getOrDefault(101, ""), type = 101),
    CategoryBook(icon = R.drawable.ic_poem, title = categoryBook.getOrDefault(102, ""), type = 102),
    CategoryBook(icon = R.drawable.ic_art, title = categoryBook.getOrDefault(103, ""), type = 103),
    CategoryBook(icon = R.drawable.ic_social, title = categoryBook.getOrDefault(104, ""), type = 104),
    CategoryBook(icon = R.drawable.ic_history, title = categoryBook.getOrDefault(105, ""), type = 105),
    CategoryBook(icon = R.drawable.ic_comic, title = categoryBook.getOrDefault(108, ""), type = 108),
    CategoryBook(icon = R.drawable.ic_science, title = categoryBook.getOrDefault(116, ""), type = 116),
    CategoryBook(icon = R.drawable.ic_financial, title = categoryBook.getOrDefault(117, ""), type = 117),
    CategoryBook(icon = R.drawable.ic_development, title = categoryBook.getOrDefault(118, ""), type = 118),
    CategoryBook(icon = R.drawable.ic_humanities, title = categoryBook.getOrDefault(119, ""), type = 119),
    CategoryBook(icon = R.drawable.ic_religion, title = categoryBook.getOrDefault(120, ""), type = 120),
    CategoryBook(icon = R.drawable.ic_computer, title = categoryBook.getOrDefault(122, ""), type = 122),
    CategoryBook(icon = R.drawable.ic_hobby, title = categoryBook.getOrDefault(124, ""), type = 124),
    CategoryBook(icon = R.drawable.ic_health, title = categoryBook.getOrDefault(126, ""), type = 126),
    CategoryBook(icon = R.drawable.ic_travel, title = categoryBook.getOrDefault(128, ""), type = 128),
)
