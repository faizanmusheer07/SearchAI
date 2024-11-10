package com.searchai.profile.manage.presentation.tablayouts.live.data.remote

import com.searchai.api.extensions.ProvideRetrofitServices
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.utils.Resource
import com.searchai.profile.commonModel.Creator
import com.searchai.profile.commonModel.Room
import com.searchai.profile.manage.presentation.tablayouts.live.domain.model.ApiCreator
import com.searchai.profile.manage.presentation.tablayouts.live.domain.model.LiveRoom
import com.searchai.profile.manage.presentation.tablayouts.live.domain.repository.LiveRoomService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class RemoteDataStore @Inject constructor(
    private val retrofitServiceProvider:ProvideRetrofitServices
):LiveRoomService {

    private val roomApiService by lazy{
        retrofitServiceProvider.createService(RemoteDataAPI::class.java)
    }

    override suspend fun getLiveRoomByUserId(userId: String): Flow<Resource<List<Room>>> = channelFlow {
        retrofitCallFlow(
            body = {roomApiService.getLiveRoomByUserId(userId)},
            tag = "LiveRoomService"
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
                    val liveRoom = resource.data?.filter { it.live }?.map { liveRoom ->
                            Room(
                                id = liveRoom.id,
                                creator =  Creator(
                                    liveRoom.creator.id,liveRoom.creator.name,liveRoom.creator.profilePicture,
                                    liveRoom.creator.channelName),
                                title = liveRoom.title,
                                schedule = liveRoom.schedule,
                                private = liveRoom.private,
                                category = liveRoom.category.toString(),
                                adminSocket = liveRoom.adminSocket,
                                admin = liveRoom.admin,
                                allowedUsers = liveRoom.allowedUsers,
                                listeners = liveRoom.listeners,
                                live = liveRoom.live,
                                message = liveRoom.message,
                                otherUsers = liveRoom.otherUsers,
                                roomCode = liveRoom.roomCode,
                                screenShared = liveRoom.screenShared,
                                subCategory = liveRoom.subCategory
                            )
                    }
                    send(Resource.Success(liveRoom))
                }
            }
        }
    }

    override suspend fun getScheduleRoomByUserId(userId: String): Flow<Resource<List<Room>>> = channelFlow {
        retrofitCallFlow(
            body = { roomApiService.getLiveRoomByUserId(userId) },
            tag = "LiveRoomService"
        ).collectLatest { resource ->
            when (resource) {
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
                    val liveRoom = resource.data?.filter { !it.live }?.map { liveRoom ->
                        Room(
                            id = liveRoom.id,
                            creator = Creator(
                                liveRoom.creator.id,
                                liveRoom.creator.name,
                                liveRoom.creator.profilePicture,
                                liveRoom.creator.channelName
                            ),
                            title = liveRoom.title,
                            schedule = liveRoom.schedule,
                            private = liveRoom.private,
                            category = liveRoom.category.toString(),
                            adminSocket = liveRoom.adminSocket,
                            admin = liveRoom.admin,
                            allowedUsers = liveRoom.allowedUsers,
                            listeners = liveRoom.listeners,
                            live = liveRoom.live,
                            message = liveRoom.message,
                            otherUsers = liveRoom.otherUsers,
                            roomCode = liveRoom.roomCode,
                            screenShared = liveRoom.screenShared,
                            subCategory = liveRoom.subCategory
                        )
                    }
                    send(Resource.Success(liveRoom))
                }
            }
        }
    }
}