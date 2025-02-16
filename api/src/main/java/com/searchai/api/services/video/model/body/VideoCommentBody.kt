package com.searchai.api.services.video.model.body

import com.google.gson.annotations.SerializedName

data class VideoCommentBody(
    @SerializedName(value = "video_id") val id: String,
    val comment: String,
)
