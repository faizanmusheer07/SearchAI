package com.searchai.api.models.auth.request

import com.squareup.moshi.Json

data class GoogleRequestBody(
    @Json(name = "auth_token")
    val authToken: String
)
