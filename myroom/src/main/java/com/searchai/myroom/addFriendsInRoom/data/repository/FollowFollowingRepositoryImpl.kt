package com.searchai.myroom.addFriendsInRoom.data.repository

import android.util.Log
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.services.followfollowing.FollowFollowingService
import com.searchai.api.utils.Resource
import com.searchai.myroom.addFriendsInRoom.domain.models.ApiResponse
import com.searchai.myroom.addFriendsInRoom.domain.models.User
import com.searchai.myroom.addFriendsInRoom.domain.repository.FollowFollowingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class FollowFollowingRepositoryImpl @Inject constructor(
    private val followingService: FollowFollowingService
) : FollowFollowingRepository {

    companion object {
        const val TAG = "FollowFollowingRepositoryImpl"
    }

    override suspend fun getFollowing(id: String): Flow<Resource<List<User>>> = channelFlow {
        retrofitCallFlow(
            body = { followingService.getFollowing(id = id) },
            tag = TAG
        ).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val following = resource.data?.data?.map { apiUser ->
                        User(
                            id = apiUser.id,
                            name = apiUser.name,
                            email = apiUser.email,
                            channelName = apiUser.channelName,
                            profilePicture = apiUser.profilePicture
                        )
                    }
                    Log.d(TAG, "getFollowing: $following")
                    send(Resource.Success(following))
                }

                is Resource.Error -> {
                    send(Resource.Error(resource.message))
                }

                else -> {
                    send(Resource.Loading)
                }
            }
        }
    }

    override suspend fun getFollower(id: String): Flow<Resource<List<User>>> = channelFlow {
        retrofitCallFlow(
            body = {
                followingService.getFollowers(id = id)
            },
            tag = "FollowFollowingRepositoryImpl"
        ).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val followers = resource.data?.data?.map { apiUser ->
                        User(
                            id = apiUser.id,
                            name = apiUser.name,
                            email = apiUser.email,
                            channelName = apiUser.channelName,
                            profilePicture = apiUser.profilePicture
                        )
                    }
                    Log.d(TAG, "getFollower: $followers")
                    send(Resource.Success(followers))
                }

                is Resource.Error -> {
                    send(Resource.Error(resource.message))
                }

                else -> {
                    send(Resource.Loading)
                }
            }
        }
    }

    override suspend fun followUser(id: String): Flow<Resource<ApiResponse>> = channelFlow {
        retrofitCallFlow(
            body = {
                followingService.followUser(id)
            }, tag = "FollowFollowingRepositoryImpl"
        ).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val follow = ApiResponse(
                        message = resource.data?.message!!,
                        status = resource.data?.status!!
                    )

                    Log.d(TAG, "followUser: $follow")
                    send(Resource.Success(follow))
                }

                is Resource.Error -> {
                    send(Resource.Error(resource.message))
                }

                else -> {
                    send(Resource.Loading)
                }
            }
        }
    }

    override suspend fun unFollowUser(id: String): Flow<Resource<ApiResponse>> = channelFlow {
        retrofitCallFlow(
            body = {
                followingService.unFollowUser(id)
            }, tag = "FollowFollowingRepositoryImpl"
        ).collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val unFollow = ApiResponse(
                        message = resource.data?.message!!,
                        status = resource.data?.status!!
                    )

                    Log.d(TAG, "followUser: $unFollow")
                    send(Resource.Success(unFollow))
                }

                is Resource.Error -> {
                    send(Resource.Error(resource.message))
                }

                else -> {
                    send(Resource.Loading)
                }
            }
        }
    }
}