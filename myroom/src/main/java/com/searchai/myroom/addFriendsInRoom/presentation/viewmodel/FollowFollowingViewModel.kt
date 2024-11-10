package com.searchai.myroom.addFriendsInRoom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.myroom.addFriendsInRoom.domain.models.User
import com.searchai.myroom.addFriendsInRoom.domain.repository.FollowFollowingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowFollowingViewModel @Inject constructor(
    private val followFollowingRepository: FollowFollowingRepository
) : ViewModel() {

    private val _followingState = MutableStateFlow<Resource<List<User>>>(Resource.Loading)
    val followingState: StateFlow<Resource<List<User>>> = _followingState

    private val _followerState = MutableStateFlow<Resource<List<User>>>(Resource.Loading)
    val followerState: StateFlow<Resource<List<User>>> = _followerState

    fun getFollowing(id : String){
        viewModelScope.launch {
            followFollowingRepository.getFollowing(id).collect { result ->
                _followingState.value = result
            }
        }
    }

    fun getFollower(id : String){
        viewModelScope.launch {
            followFollowingRepository.getFollower(id).collect { result ->
                _followerState.value = result
            }
        }
    }
}