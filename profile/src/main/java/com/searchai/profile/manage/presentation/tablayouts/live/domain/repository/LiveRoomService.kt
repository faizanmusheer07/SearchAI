package com.searchai.profile.manage.presentation.tablayouts.live.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.profile.commonModel.Room
import com.searchai.profile.manage.presentation.tablayouts.live.domain.model.LiveRoom
import kotlinx.coroutines.flow.Flow

interface LiveRoomService {
    suspend fun getLiveRoomByUserId(userId:String): Flow<Resource<List<Room>>>
    suspend fun getScheduleRoomByUserId(userId:String): Flow<Resource<List<Room>>>
}