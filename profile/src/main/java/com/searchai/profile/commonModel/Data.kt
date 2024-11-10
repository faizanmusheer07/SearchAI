package com.searchai.profile.commonModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "rooms")
    val rooms: List<Room>
)