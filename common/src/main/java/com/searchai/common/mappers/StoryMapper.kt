package com.searchai.common.mappers

import cessini.technology.model.Viewer
import com.searchai.api.services.story.model.response.ApiStory

fun ApiStory.toModel() = Viewer(
    id = id,
    profileId = profileId,
    caption = caption,
    thumbnail = thumbnail,
    duration = duration.toIntOrNull() ?: 0,
    url = url,
    timestamp = timestamp,
)
