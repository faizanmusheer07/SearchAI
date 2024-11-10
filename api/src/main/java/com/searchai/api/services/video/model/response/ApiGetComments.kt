package com.searchai.api.services.video.model.response



data class ApiGetComments(
    val data: ApiCommentData = ApiCommentData(),
)

data class ApiCommentData(
    val comments: List<ApiComment> = emptyList(),
)
