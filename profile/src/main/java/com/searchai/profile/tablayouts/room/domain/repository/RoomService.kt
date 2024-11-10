package com.searchai.profile.tablayouts.room.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import kotlinx.coroutines.flow.Flow

interface RoomService {
    suspend fun getRoomStream(id:String): Flow<Resource<List<VideoModel>>>
}