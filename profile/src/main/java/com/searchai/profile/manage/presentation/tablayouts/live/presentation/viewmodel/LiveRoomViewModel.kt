package com.searchai.profile.manage.presentation.tablayouts.live.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.checkNetwork.CheckInternet
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.profile.commonModel.Room
import com.searchai.profile.manage.presentation.tablayouts.live.data.remote.RemoteDataStore
import com.searchai.profile.manage.presentation.tablayouts.live.domain.model.LiveRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class LiveRoomViewModel @Inject constructor(
    private val remoteDataStore: RemoteDataStore,
    private val checkInternet: CheckInternet,
    val app:Application
):ViewModel() {

    private var _uiState = MutableStateFlow<Resource<List<Room>>>(Resource.Loading)
    val uiState:Flow<Resource<List<Room>>> get() = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading
        )

    private var _scheduleRoom = MutableStateFlow<Resource<List<Room>>>(Resource.Loading)
    val scheduleRoom:Flow<Resource<List<Room>>> get() = _scheduleRoom
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading
        )

    fun fetchLiveRoom(userId:String){
        if (checkInternet.hasInternetConnection(app)){
            viewModelScope.launch {
                remoteDataStore.getLiveRoomByUserId(userId)
                    .collect{ response->
                        _uiState.value = response
                    }
            }
        }else{
            _uiState.value = Resource.Error(NetworkStatus.NoInternet.toString())
        }
    }

    fun fetchScheduleRoom(userId:String){
        if (checkInternet.hasInternetConnection(app)){
            viewModelScope.launch {
                remoteDataStore.getScheduleRoomByUserId(userId)
                    .collect{ response->
                        _scheduleRoom.value = response
                    }
            }
        }else{
            _scheduleRoom.value = Resource.Error(NetworkStatus.NoInternet.toString())
        }
    }

}