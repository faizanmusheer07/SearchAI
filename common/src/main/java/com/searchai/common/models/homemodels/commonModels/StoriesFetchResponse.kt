package com.searchai.common.models.homemodels.commonModels

data class StoriesFetchResponse(
    var id: String?,
    var name: String?,
    var profile_picture: String?,
    var channel_name: String?,
    var stories: ArrayList<StoriesFetchModel>?
)
