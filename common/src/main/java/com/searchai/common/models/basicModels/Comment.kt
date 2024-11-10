package com.searchai.common.models.basicModels

data class Comment(
    val id: String,
    val profileId: String,
    val channelName: String,
    val profilePicture: String,
    val text: String,
)
