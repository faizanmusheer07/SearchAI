package com.searchai.profile.followandfollower.domain.model

import com.searchai.common.R


data class FollowItemModels(
    val id:String,
    val profile_picture:Int,
    val name: String,
    val channel_name : String,
    val email : String,
)

//val followerModel = listOf(
//    FollowItemModels(1.toString(), R.drawable.ic_user_defolt_avator,"Ankur","@Ankur001"),
//    FollowItemModels(2.toString(), R.drawable.ic_user_defolt_avator,"Kapil","@Kapil001"),
//    FollowItemModels(3, R.drawable.ic_user_defolt_avator,"Gursimar","@Gursimar001"),
//    FollowItemModels(4, R.drawable.ic_user_defolt_avator,"Aditya","@Aditya001"),
//    FollowItemModels(5, R.drawable.ic_user_defolt_avator,"Kanak","@Kanak001"),
//    FollowItemModels(6, R.drawable.ic_user_defolt_avator,"Ujjwal","@Ujjwal001")
//
//)
