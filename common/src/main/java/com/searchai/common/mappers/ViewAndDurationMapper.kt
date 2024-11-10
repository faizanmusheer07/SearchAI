package com.searchai.common.mappers

import com.searchai.api.services.story.model.response.ApiViewAndDuration
import com.searchai.common.models.basicModels.ViewAndDuration


fun ApiViewAndDuration.toModel() = ViewAndDuration(
    views = views,
    duration = duration,
)
