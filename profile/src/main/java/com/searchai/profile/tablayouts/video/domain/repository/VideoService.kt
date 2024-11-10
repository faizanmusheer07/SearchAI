package com.searchai.profile.tablayouts.video.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import kotlinx.coroutines.flow.Flow

interface VideoService {
    suspend fun getVideoStream(id:String): Flow<Resource<List<VideoModel>>>
}