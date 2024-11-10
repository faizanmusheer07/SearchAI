package com.searchai.profile.followandfollower.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.profile.followandfollower.data.remote.FollowAPIService
import com.searchai.profile.followandfollower.domain.model.FollowItemModels
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface FollowRepository{
    suspend fun getFollower(userId : String): Flow<Resource<List<FollowItemModels>>>
    suspend fun getFollowing(userId: String): Flow<Resource<List<FollowItemModels>>>

}