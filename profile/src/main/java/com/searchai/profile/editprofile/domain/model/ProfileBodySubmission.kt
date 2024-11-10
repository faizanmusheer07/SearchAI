package com.searchai.profile.editprofile.domain.model

import com.squareup.moshi.Json

data class ProfileBodySubmission(
    val name: String?,
    @Json(name = "channel_name")
    val channelName: String?,
    @Json(name = "area_of_expert")
    val areaOfExpert: String?,
    val provider: String?,
    val bio: String?,
    @Json(name= "web_address")
    val webAddress: String?,
    val location: String?,
    @Json(name = "profile_picture")
    val profilePicture: String?
)