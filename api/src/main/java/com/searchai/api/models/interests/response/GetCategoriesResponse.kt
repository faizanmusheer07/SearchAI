package com.searchai.api.models.interests.response

import com.squareup.moshi.Json

data class GetCategoriesResponse(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val branchId: String,
    @Json(name = "category")
    val category: List<CategoryResponse>
)

data class CategoryResponse(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val categoryName: String,
)
