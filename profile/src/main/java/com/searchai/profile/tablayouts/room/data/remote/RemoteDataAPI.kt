package com.searchai.profile.tablayouts.room.data.remote

import com.searchai.api.constants.api.params.ApiParameters
import com.searchai.profile.tablayouts.room.constants.RoomConstants
import com.searchai.profile.tablayouts.video.constants.VideoConstants
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteDataAPI {

    @GET(RoomConstants.RECORD_END_POINT)
    suspend fun getProfileRoom(
        @Query(ApiParameters.ID) id: String,
    ): Response<List<VideoModel>>
}