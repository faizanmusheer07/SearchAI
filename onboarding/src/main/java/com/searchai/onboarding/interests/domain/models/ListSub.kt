package com.searchai.onboarding.interests.domain.models


data class ListSub(
    val id: String,
    val categoryName: String,
    val subcategories: List<SubCategory>,
)