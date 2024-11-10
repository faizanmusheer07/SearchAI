package com.searchai.api.models.myroom

import com.squareup.moshi.Json

data class UserRegistrationBody(
    @Json(name = "device_id") val uuid: String,
    val categories: Set<String>,
    val sub_categories: Set<String>
)