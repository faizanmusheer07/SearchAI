package com.searchai.common.models.homemodels.commonModels

import android.graphics.Bitmap

data class StoryModel(
    var caption: String?,
    var thumbnail: Bitmap?,
    var duration: Int,
    var upload_file: String?
)