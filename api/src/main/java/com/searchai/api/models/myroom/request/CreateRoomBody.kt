package com.searchai.api.models.myroom.request

import com.squareup.moshi.Json

data class CreateRoomBody(
    @Json(name = "create_room")
    val title:String,
    val category:List<Map<String,Set<String>>>,
    val schedule:String,
    val private:Boolean
)