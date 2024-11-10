package com.searchai.myroom.addFriendsInRoom.domain.repository

import com.searchai.api.utils.Resource
import com.searchai.myroom.addFriendsInRoom.domain.models.ApiResponse
import com.searchai.myroom.addFriendsInRoom.domain.models.User
import kotlinx.coroutines.flow.Flow

interface aFollowFollowingRepository {
    suspend fun getFollowing(id: String): Flow<Resource<List<User>>>
    suspend fun getFollower(id: String): Flow<Resource<List<User>>>
    suspend fun followUser(id: String): Flow<Resource<ApiResponse>>
    suspend fun unFollowUser(id: String): Flow<Resource<ApiResponse>>
}