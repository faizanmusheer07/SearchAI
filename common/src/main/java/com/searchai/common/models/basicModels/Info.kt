package com.searchai.common.models.basicModels

import com.google.gson.annotations.SerializedName

data class Info (
    val message: List<MessageI>,
    val status: Boolean
)

data class MessageI (
    @SerializedName("_id")
    val id: String,

    val title: String,
    val description: String,
    val image: String,
    val views: String,
    val timestamp: String
)
