package com.searchai.profile.commonModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRoomModel(
    @Json(name = "data")
    val roomData: Data,
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: Boolean
)