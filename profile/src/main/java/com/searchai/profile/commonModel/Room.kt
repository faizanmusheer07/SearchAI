package com.searchai.profile.commonModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Room(
    @Json(name = "admin")
    val admin: String,
    @Json(name = "adminSocket")
    val adminSocket: String,
    @Json(name = "allowedUsers")
    val allowedUsers: List<Any>,
    @Json(name = "category")
    val category: String,
    @Json(name = "creator")
    val creator: Creator,
    @Json(name = "_id")
    val id: String,
    @Json(name = "listeners")
    val listeners: List<Any>,
    @Json(name = "live")
    val live: Boolean,
    @Json(name = "message")
    val message: List<Any>,
    @Json(name = "otherUsers")
    val otherUsers: List<Any>,
    @Json(name = "private")
    val private: Boolean,
    @Json(name = "room_code")
    val roomCode: String,
    @Json(name = "schedule")
    val schedule: Long,
    @Json(name = "screenShared")
    val screenShared: List<Any>,
    @Json(name = "sub_category")
    val subCategory: List<Any>,
    @Json(name = "title")
    val title: String
)