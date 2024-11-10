package com.searchai.profile.manage.presentation.tablayouts.request.data

import com.searchai.api.constants.api.params.ApiParameters
import com.searchai.profile.manage.presentation.tablayouts.request.constant.Constant
import com.searchai.profile.manage.presentation.tablayouts.request.domain.model.RequestModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RequestRemoteAPI {

    @Headers(ApiParameters.NO_AUTH_HEADER)
    @GET(Constant.GET_PENDING_REQUEST_END_POINT)
    suspend fun getAllRequest(
        @Query(ApiParameters.ROOM_CODE) roomCode:String
    ):Response<List<RequestModel>>
}