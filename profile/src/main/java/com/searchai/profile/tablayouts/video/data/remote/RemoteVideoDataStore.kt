package com.searchai.profile.tablayouts.video.data.remote

import com.searchai.api.extensions.ProvideRetrofitServices
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.utils.Resource
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import com.searchai.profile.tablayouts.video.domain.repository.VideoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class RemoteVideoDataStore @Inject constructor(
    private val retrofitServiceProvider: ProvideRetrofitServices
): VideoService {

    private val roomServiceApi by lazy {
        retrofitServiceProvider.createService(RemoteVideoDataAPI::class.java)
    }

    override suspend fun getVideoStream(id:String): Flow<Resource<List<VideoModel>>> = channelFlow {
        retrofitCallFlow(
            body = {roomServiceApi.getProfileVideo(id)},
            tag = "getRoomStream"
        ).collectLatest { resources->
            when(resources){
                is Resource.Error -> {
                    send(Resource.Error(resources.message))
                }
                is Resource.Loading -> {
                    send(Resource.Loading)
                }
                is Resource.Idle -> {
                    send(Resource.Idle)
                }
                is Resource.Success -> {
                    val rooms = resources.data?.map { room->
                        VideoModel(
                            category = room.category,
                            filename = room.filename,
                            thumbnail = room.thumbnail,
                            title = room.title,
                            username = room.username,
                            userid = room.userid
                        )
                    }
                    send(Resource.Success(rooms))
                }

            }

        }
    }

}