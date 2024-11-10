package com.searchai.common.models.basicModels

data class ProfileStories(
    val profileId: String,
    val picture: String,
    val viewers: List<Viewer>,
)
