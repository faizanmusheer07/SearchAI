package com.searchai.common.models.homemodels.commonModels

data class StoryResponse(
    var id : Int ,
    var caption : String?,
    var thumbnail : String?,
    var duration : Int,
    var upload_file : String,
    var timestamp : String,
)