package com.searchai.profile.manage.presentation.tablayouts.live.domain.model

import com.squareup.moshi.Json

data class LiveRoom(
    val id: Int,
    val creator: ApiCreator = ApiCreator(),
    val title: String = "",
    val schedule: Long = 0L,
    val private: Boolean = false,
    val category: List<String> = emptyList(),
    val live: Boolean = false,
    @Json(name = "room_code") val code: String = "",
)


data class ApiCreator(
    @Json(name = "_id") val id: String = "",
    val name: String = "",
    @Json(name = "profile_picture") val profilePicture: String = "",
    @Json(name = "channel_name") val channelName: String = "",
)
