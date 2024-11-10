package com.searchai.profile.followandfollower.data.remote

import com.searchai.api.extensions.ProvideRetrofitServices
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.utils.Resource
import com.searchai.profile.followandfollower.domain.model.FollowItemModels
import com.searchai.profile.followandfollower.domain.repository.FollowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class FollowRemoteDataStore @Inject constructor(
    private val retrofitServiceProvider: ProvideRetrofitServices
) : FollowRepository {
    private val followServiceApi by lazy {
        retrofitServiceProvider.createService(FollowAPIService::class.java)
    }

    override suspend fun getFollower(userId: String): Flow<Resource<List<FollowItemModels>>> = channelFlow {
        retrofitCallFlow(
            body = { followServiceApi.getFollowers(userId)},
            tag = "getFollowerStream"
        ).collectLatest { resource ->
            when(resource) {
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
                    val followers = resource.data?.map { follower ->
                        FollowItemModels(
                            id = follower.id,
                            profile_picture = follower.profile_picture,
                            name = follower.name,
                            channel_name = follower.channel_name,
                            email = follower.email
                        )
                    }
                    send(Resource.Success(followers))
                }
            }
        }
    }


    override suspend fun getFollowing(userId: String): Flow<Resource<List<FollowItemModels>>> = channelFlow {
        retrofitCallFlow(
            body = { followServiceApi.getFollowing(userId) },
            tag = "getFollowingStream"
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
                    val following = resource.data?.map { following ->
                        FollowItemModels(
                            id = following.id,
                            profile_picture = following.profile_picture,
                            name = following.name,
                            channel_name = following.channel_name,
                            email = following.email
                        )
                    }
                    send(Resource.Success(following))
                }
            }

        }
    }

}