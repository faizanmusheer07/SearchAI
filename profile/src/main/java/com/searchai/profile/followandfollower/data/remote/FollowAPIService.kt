package com.searchai.profile.followandfollower.data.remote

import com.searchai.api.constants.api.params.ApiParameters
import com.searchai.api.constants.follow.FollowConstants
import com.searchai.profile.followandfollower.domain.model.FollowItemModels
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FollowAPIService {
    @GET(FollowConstants.FOLLOWER_ENDPOINT)
    suspend fun getFollowers(
        @Query(ApiParameters.ID) id: String
    ): Response<List<FollowItemModels>>

    @GET(FollowConstants.FOLLOWING_ENDPOINT)
    suspend fun getFollowing(
        @Query(ApiParameters.ID) id: String
    ): Response<List<FollowItemModels>>

}