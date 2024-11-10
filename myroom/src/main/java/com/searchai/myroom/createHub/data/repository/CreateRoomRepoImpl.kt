package com.searchai.myroom.createHub.data.repository

import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.models.myroom.request.CreateRoomBody
import com.searchai.api.models.myroom.response.CreateRoomResponse
import com.searchai.api.services.myroom.MyRoomService
import com.searchai.api.utils.Resource
import com.searchai.myroom.createHub.domain.repository.CreateRoomRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateRoomRepoImpl @Inject constructor(private val roomApi: MyRoomService) : CreateRoomRepo {
    override suspend fun createRoom(createRoomRequest: CreateRoomBody): Flow<Resource<CreateRoomResponse>> =
        retrofitCallFlow({
            roomApi.createRoom(createRoomRequest)
        })
}