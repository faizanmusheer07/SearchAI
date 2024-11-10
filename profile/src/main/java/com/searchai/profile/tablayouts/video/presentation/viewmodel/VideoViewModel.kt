package com.searchai.profile.tablayouts.video.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.checkNetwork.CheckInternet
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.profile.tablayouts.video.data.remote.RemoteVideoDataStore
import com.searchai.profile.tablayouts.video.domain.model.VideoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val remoteVideoDataStore: RemoteVideoDataStore,
    private val checkInternet: CheckInternet,
    val app:Application
):ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<VideoModel>>>(Resource.Idle)
    val uiState:StateFlow<Resource<List<VideoModel>>> get() = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading
        )

    fun fetchVideoStream(id:String){
        if (checkInternet.hasInternetConnection(app)){
            viewModelScope.launch {
                remoteVideoDataStore.getVideoStream(id)
                    .collect{ response->
                        _uiState.value = response
                    }
            }
        }else{
            _uiState.value = Resource.Error(NetworkStatus.NoInternet.toString())
        }

    }

}