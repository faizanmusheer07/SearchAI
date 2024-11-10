package com.searchai.common.models.basicModels

import com.google.gson.annotations.SerializedName

data class Component (
    val message: MessageD? = null,
    val status: Boolean? = null
)

data class MessageD (
    @SerializedName("Technology")
    val technology: List<HealthFitness>? = null,

    @SerializedName("Trending Technology")
    val trendingTechnology: List<HealthFitness>? = null,

    @SerializedName("IT")
    val it: List<HealthFitness>? = null,

    @SerializedName("Food")
    val food: List<HealthFitness>? = null,

    @SerializedName("Knowledge")
    val knowledge: List<HealthFitness>? = null
)

data class HealthFitness (
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("profile")
    val profile: ProfileI? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("duration")
    val duration: String? = null,

    @SerializedName("category")
    val category: List<String>? = null,
    //val category: String? = null,

    @SerializedName("upload_file")
    val uploadFile: String? = null,

    @SerializedName("timestamp")
    val timestamp: Double? = null
)

data class ProfileI (
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("channel_name")
    val channelName: String? = null,

    @SerializedName("profile_picture")
    val profilePicture: String? = null
)




data class ComponentRecorded (
    val message: MessageRecorded? = null,
    val status: Boolean? = null
)

data class MessageRecorded (
    @SerializedName("Technology")
    val trendingTechnology: List<itemRecorded>? = null,

    @SerializedName("Trending Technology")
    val trendingNews: List<itemRecorded>? = null,

    @SerializedName("IT")
    val entertainment: List<itemRecorded>? = null,

    @SerializedName("Food")
    val healthFitness: List<itemRecorded>? = null,

    @SerializedName("Knowledge")
    val knowledgeCareers: List<itemRecorded>? = null
)



data class itemRecorded(
    @SerializedName("title")
    val title:String?=null,

    @SerializedName("thumbnail")
    val thumbnail: String?=null,

    @SerializedName("recordingsFilePath")
    val record: String? = null,

    @SerializedName("creator")
    val creator: ProfileI
)