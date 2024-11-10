package com.searchai.profile.followandfollower.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.checkNetwork.CheckInternet
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.profile.followandfollower.data.remote.FollowRemoteDataStore
import com.searchai.profile.followandfollower.domain.model.FollowItemModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val followRemoteDataStore: FollowRemoteDataStore,
    private val checkInternet: CheckInternet,
    val app: Application
) : ViewModel() {

    private val _followers = MutableStateFlow<Resource<List<FollowItemModels>>>(Resource.Loading)
    val followers: Flow<Resource<List<FollowItemModels>>> get() = _followers

    private val _following = MutableStateFlow<Resource<List<FollowItemModels>>>(Resource.Loading)
    val following: Flow<Resource<List<FollowItemModels>>> get() = _following

    fun fetchFollowersStream(userId: String) {
        if (checkInternet.hasInternetConnection(app)){
            viewModelScope.launch {
                followRemoteDataStore.getFollower(userId).collect { resource ->
                    _followers.value = resource
                }
            }
        }else{
            _followers.value = Resource.Error(NetworkStatus.NoInternet.toString())
        }
    }

    fun fetchFollowingStream(userId: String) {
        if (checkInternet.hasInternetConnection(app)){
            viewModelScope.launch {
                followRemoteDataStore.getFollowing(userId).collect { resource ->
                    _following.value = resource
                }
            }
        }else{
            _following.value = Resource.Error(NetworkStatus.NoInternet.toString())
        }

    }

}