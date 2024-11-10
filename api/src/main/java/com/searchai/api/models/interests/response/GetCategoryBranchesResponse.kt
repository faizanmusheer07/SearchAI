package com.searchai.api.models.interests.response

import com.squareup.moshi.Json


data class GetCategoryBranchesResponse(
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val branchId: String
)