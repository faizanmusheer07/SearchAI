package com.searchai.api.models.followfollowing

import com.squareup.moshi.Json

data class ApiFollowerFollowing(
    val message: String,
    val status: Boolean,
    val data: List<ApiUser> = emptyList(),
)

data class ApiUser(
    @Json(name = "_id") val id: String,
    val name: String,
    val email: String,
    @Json(name = "channel_name") val channelName: String,
    @Json(name = "profile_picture") val profilePicture: String
)