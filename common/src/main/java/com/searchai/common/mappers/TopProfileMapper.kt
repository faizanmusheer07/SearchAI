package com.searchai.common.mappers

import cessini.technology.model.TopProfile
import cessini.technology.newapi.services.explore.model.response.ApiTopProfile

fun ApiTopProfile.toModel() = TopProfile(
    id = id,
    name = name,
    channelName = channelName,
    profilePicture = profilePicture,
    area_of_expert= if(!area_of_expert.isNullOrEmpty()) area_of_expert else ""

//    is_following = is_following
)
