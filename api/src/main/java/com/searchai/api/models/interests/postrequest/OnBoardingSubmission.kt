package com.searchai.api.models.interests.postrequest

import com.squareup.moshi.Json

data class OnBoardingSubmission(
    @Json(name = "device_id")
    val deviceId: String,
    @Json(name = "language")
    val language: String,
    @Json(name = "categories")
    val categories: Map<String, Map<String, List<String>>>
)
