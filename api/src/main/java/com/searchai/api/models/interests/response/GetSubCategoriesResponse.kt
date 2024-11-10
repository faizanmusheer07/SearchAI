package com.searchai.api.models.interests.response

import com.squareup.moshi.Json

data class GetSubCategoriesResponse(
    @Json(name = "_id")
    val id: String,
    @Json(name = "category")
    val category: List<Category>,
)

data class Category(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val categoryName: String,
    @Json(name = "subs")
    val subcategories: List<SubCategory>,
)

data class SubCategory(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val subCategoryName: String
)