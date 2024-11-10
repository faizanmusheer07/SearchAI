package com.searchai.profile.commonModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Creator(
    @Json(name = "channel_name")
    val channelName: String,
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "profile_picture")
    val profilePicture: String
)