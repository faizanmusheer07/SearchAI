package com.searchai.common.models.basicModels

data class TopProfile(
    val id: String,
    val name: String,
    val channelName: String,
    val profilePicture: String,
    var area_of_expert:String
//    val is_following: Boolean?
)
