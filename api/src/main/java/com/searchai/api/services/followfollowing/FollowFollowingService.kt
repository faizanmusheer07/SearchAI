package com.searchai.api.services.followfollowing

import com.searchai.api.constants.followfollowing.FollowFollowingConstants.FOLLOWER_ENDPOINT
import com.searchai.api.constants.followfollowing.FollowFollowingConstants.FOLLOWING_ENDPOINT
import com.searchai.api.models.followfollowing.ApiFollowerFollowing
import com.searchai.api.models.followfollowing.ApiFollowResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

interface FollowFollowingService {

    @GET(value = FOLLOWING_ENDPOINT)
    suspend fun getFollowing(
        @Query(value = "id") id: String,
    ): Response<ApiFollowerFollowing>

    @GET(value = FOLLOWER_ENDPOINT)
    suspend fun getFollowers(
        @Query(value = "id") id: String
    ): Response<ApiFollowerFollowing>

    @POST(FOLLOWING_ENDPOINT)
    suspend fun followUser(
        @Body apiUserId: String,
    ): Response<ApiFollowResponse>

    @HTTP(method = "DELETE", path = FOLLOWING_ENDPOINT, hasBody = true)
    suspend fun unFollowUser(
        @Body apiUserId: String,
    ): Response<ApiFollowResponse>
}

//    @GET(FollowConstants.FOLLOWER_ENDPOINT)
//    suspend fun getFollowers(
//        @Query(ApiParameters.ID) id: String
//    ): Response<List<FollowItemModels>>
//
//    @GET(FollowConstants.FOLLOWING_ENDPOINT)
//    suspend fun getFollowing(
//        @Query(ApiParameters.ID) id: String
//    ): Response<List<FollowItemModels>>