package com.searchai.profile.setting.domain.models

import com.searchai.common.R

data class OtherSettings(
    val id:Int,
    val title:String,
    val image:Int
)

val otherSetting = listOf(
    OtherSettings(1,"Payments and Payouts", R.drawable.ic_myicon),
    OtherSettings(2,"Log out",R.drawable.ic_logout)
)
