package com.searchai.api.models.myroom

import com.squareup.moshi.Json

data class GetMyRoomSubCategoriesResponse(
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
