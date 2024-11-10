package com.searchai.common.models.homemodels.commonModels

import com.searchai.common.models.exploremodels.ProfileModel


data class CommentModel(
    var id: Int,
    var profile: ProfileModel,
    var comment: String,
    var timestamp: String
)