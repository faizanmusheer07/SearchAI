package com.searchai.profile.setting.domain.models

data class TextModel(
    val id:Int,
    val title:String
)

val textSetting = listOf(
    TextModel(1,"Edit Profile"),
    TextModel(2,"Discovery"),
    TextModel(3,"Monetization"),
    TextModel(4,"Payments and payouts"),
)