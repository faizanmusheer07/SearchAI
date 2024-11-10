package com.searchai.profile.manage.presentation.tablayouts.request.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.profile.manage.presentation.tablayouts.request.domain.model.RequestModel
import kotlinx.coroutines.flow.Flow

interface RequestService {
    suspend fun getAllRoomJoinRequest(roomCode:String): Flow<Resource<List<RequestModel>>>
}