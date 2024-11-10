package com.searchai.common.mappers


fun ApiProfileStories.toModel() = ProfileStories(
    profileId = profileId,
    picture = picture,
    viewers = stories.map { it.toModel() }
)
