package com.searchai.common.kotlinExtentions.interestChip.models


data class ListSub(
    val id: String,
    val categoryName: String,
    val subcategories: List<SubCategory>,
)