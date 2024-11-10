package com.searchai.myroom.selectCategories.domain.models


data class ListSub(
    val id: String,
    val categoryName: String,
    val subcategories: List<SubCategory>,
)