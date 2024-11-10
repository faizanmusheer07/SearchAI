package com.searchai.profile.manage.presentation.tablayouts.request.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.checkNetwork.CheckInternet
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.profile.commonModel.Room
import com.searchai.profile.manage.presentation.tablayouts.request.data.RequestRemoteAPI
import com.searchai.profile.manage.presentation.tablayouts.request.data.RequestRemoteDataStore
import com.searchai.profile.manage.presentation.tablayouts.request.domain.model.RequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val remoteDataStore: RequestRemoteDataStore,
    private val checkInternet: CheckInternet,
    val app: Application
):ViewModel() {

    private var _request = MutableStateFlow<Resource<List<RequestModel>>>(Resource.Loading)
    val request: Flow<Resource<List<RequestModel>>>
        get() = _request
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading
        )

    fun fetchRequestResponse(roomId:String){
        if (checkInternet.hasInternetConnection(app)){
            viewModelScope.launch {
                remoteDataStore.getAllRoomJoinRequest(roomId)
                    .collect{ response->
                        _request.value = response
                    }
            }
        }else{
            _request.value = Resource.Error(NetworkStatus.NoInternet.toString())
        }
    }

}