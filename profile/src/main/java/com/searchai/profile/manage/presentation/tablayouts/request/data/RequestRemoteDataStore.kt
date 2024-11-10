package com.searchai.profile.manage.presentation.tablayouts.request.data

import com.searchai.api.extensions.ProvideRetrofitServices
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.utils.Resource
import com.searchai.profile.commonModel.Creator
import com.searchai.profile.commonModel.Room
import com.searchai.profile.manage.presentation.tablayouts.request.domain.model.RequestModel
import com.searchai.profile.manage.presentation.tablayouts.request.domain.repository.RequestService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class RequestRemoteDataStore @Inject constructor(
    private val retrofitServices: ProvideRetrofitServices
):RequestService{

    private val requestServiceApi by lazy {
        retrofitServices.createService(RequestRemoteAPI::class.java)
    }

    override suspend fun getAllRoomJoinRequest(roomCode: String): Flow<Resource<List<RequestModel>>> = channelFlow {
        retrofitCallFlow(
            body = { requestServiceApi.getAllRequest(roomCode) },
            tag = "roomRequest"
        ).collectLatest { resource ->
            when(resource){
                is Resource.Error -> {
                    send(Resource.Error(resource.message))
                }
                is Resource.Loading -> {
                    send(Resource.Loading)
                }
                is Resource.Idle -> {
                    send(Resource.Idle)
                }
                is Resource.Success -> {
                    val request = resource.data?.map { roomRequest ->
                        RequestModel(
                            id = roomRequest.id,
                            channelName = roomRequest.channelName,
                            roomId = roomRequest.roomId,
                            userName = roomRequest.userName,
                            userImage = roomRequest.userImage
                        )
                    }
                    send(Resource.Success(request))
                }
            }

        }
    }


}