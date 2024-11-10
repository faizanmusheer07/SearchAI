package com.searchai.common.models.basicModels

data class StoryLikeAndView(
    val id: String,
    val likes: Int,
    val viewAndDuration: ViewAndDuration,
)
