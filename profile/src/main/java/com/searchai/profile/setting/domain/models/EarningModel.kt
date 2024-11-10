package com.searchai.profile.setting.domain.models

data class EarningModel(
    val id:Int,
    val hub:Int,
    val viewProfileCount:Int,
    val revenue:Int
)

val earningCounts = listOf(
    EarningModel(1,0,0,0)
)