package com.searchai.profile.manage.presentation.tablayouts.live.data.remote

import com.searchai.api.constants.api.params.ApiParameters
import com.searchai.profile.commonModel.Room
import com.searchai.profile.manage.presentation.tablayouts.live.constants.Constant
import com.searchai.profile.manage.presentation.tablayouts.live.domain.model.LiveRoom
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RemoteDataAPI {

    @Headers(ApiParameters.NO_AUTH_HEADER)
    @GET(Constant.LIVE_ROOMS)
    suspend fun getLiveRoomByUserId(
        @Query(ApiParameters.ID) id:String
    ):Response<List<Room>>
}