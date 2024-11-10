package com.searchai.api.models.interests.response

import com.squareup.moshi.Json

data class OnBoardingSubmissionResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "deviceId")
    val deviceId: String?,
    @Json(name = "status")
    val status: Boolean?,
)
