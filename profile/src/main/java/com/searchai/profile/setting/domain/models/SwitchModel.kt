package com.searchai.profile.setting.domain.models

data class SwitchModel(
    val id:Int,
    val title:String,
    val helperText:String
)

val  switchSetting = listOf(
    SwitchModel(1,"Dark theme","Change theme"),
    SwitchModel(2,"Autoplay","Keep on viewing to similar video track when your video ends"),
    SwitchModel(3,"Video Quality","Smart adoptive(reduce data usage when network is metered or slow"),
    SwitchModel(4,"Camera Advancement","Recommended mode base on device (Tab & hold recording)"),
    SwitchModel(5,"Notification","Set your new video trend notification on"),
    SwitchModel(6,"Data Saver","Set your new video quality to low (equivalent to 40kbit/s) and disables creator canvases"),
)
