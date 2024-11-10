package com.searchai.myroom.createHub.domain.repository

import com.searchai.api.models.myroom.request.CreateRoomBody
import com.searchai.api.models.myroom.response.CreateRoomResponse
import com.searchai.api.services.myroom.MyRoomService
import com.searchai.api.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreateRoomRepo {
    suspend fun createRoom(createRoomRequest: CreateRoomBody): Flow<Resource<CreateRoomResponse>>
}