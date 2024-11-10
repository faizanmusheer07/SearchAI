package com.searchai.api.models.auth.response

import com.squareup.moshi.Json

data class GoogleResponseBody(
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "data")
    val data: ProfileData
)

data class ProfileData(
    @Json(name = "profile")
    val profile: Profile,
    @Json(name = "token")
    val token: Token
)

data class Profile(
    @Json(name = "_id")
    val id: String,
    @Json(name = "user_id")
    val userId: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "channel_name")
    val channelName: String,
    @Json(name = "area_of_expert")
    val areaOfExpert: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "provider")
    val provider: String,
    @Json(name = "bio")
    val bio: String,
    @Json(name = "web_address")
    val webAddress: String,
    @Json(name = "location")
    val location: String,
    @Json(name = "verified")
    val verified: Boolean,
    @Json(name = "profile_picture")
    val profilePicture: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "follower_count")
    val followerCount: Int,
    @Json(name = "following_count")
    val followingCount: Int
)

data class Token(
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "access_token")
    val accessToken: String
)