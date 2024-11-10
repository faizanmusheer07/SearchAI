package com.searchai.myroom.createHub.presentation.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.common.kotlinExtentions.base.extensions.request
import com.searchai.common.models.basicModels.Subcategory
import com.searchai.common.preferences.UserIdentifierPreferences
import com.searchai.explore.data.repository.RegistrationRepository
import com.searchai.myroom.addFriendsInRoom.data.repository.FollowFollowingRepositoryImpl.Companion.TAG
import com.searchai.myroom.createHub.domain.repository.CreateRoomRepo
import com.searchai.myroom.createRoom.presentation.fragments.CreateRoomFragment
import com.searchai.myroom.databinding.FragmentCreateRoomBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewmodel @Inject constructor(
    private val roomRepository: CreateRoomRepo,
    private val userIdentifierPreferences: UserIdentifierPreferences,
    private val registrationRepository: RegistrationRepository
): BaseViewModel<CreateRoomViewmodel.Event>() {

    val bottomSheetDraggedState = MutableLiveData<Float>()
    val roomTitle = MutableLiveData<String>()
    val time = MutableLiveData<Long>()
    var data= MutableLiveData<Map<String, List<Subcategory>>>()
    var flag=true

    var categorySet = mutableSetOf<String>()

    //Specially made State flow in CreateRoomSharedViewModel to observe from CreateRoomFragment
    val categorySetStateFlow = MutableStateFlow(categorySet)
    var subCategorySet = mutableSetOf<Int>()
    var loggedIn = MutableLiveData<Boolean>()

    private val _requestInProgress = MutableLiveData(false)

    val selectedRoomCategories = mutableSetOf<Int>()

    fun updateDraggedState(state:Float) {
        bottomSheetDraggedState.value = state
    }

    fun isLoggedIn(){
        loggedIn.value = userIdentifierPreferences.loggedIn
    }
    fun createRoom() {
        validate(reason = "Enter Title!", { return }) { roomTitle.value != null && roomTitle.value!!.isNotBlank() && roomTitle.value!!.isNotEmpty()}
        validate(reason = "Choose Time!", { return }) { time.value != null }
        validate(reason = "Choose Topic of Hub!", { return }) { !categorySet.isNullOrEmpty() }
        validate(reason = "Log in to Create Hub", { return }) { userIdentifierPreferences.loggedIn }

        return request(
            _requestInProgress,
            block = {
                roomRepository.createRoom(
                    title = roomTitle.value!!,
                    time = time.value!!,
                    private = false,
                    categories = categorySet,
                    roomCode = CreateRoomFragment.current_room_code

                )
            },
            onSuccess = { Event.RoomCreated(name = it).send() },
            onFailure = { Event.Failed(it.message.orEmpty()).send() },
        )
    }
    fun createInstantRoom(binding: FragmentCreateRoomBinding) {
        validate(reason = "Enter Title!", { return }) { roomTitle.value != null && roomTitle.value!!.isNotBlank() && roomTitle.value!!.isNotEmpty()}
        validate(reason = "Choose Time!", { return }) { time.value != null }
        validate(reason = "Choose Topic of Hub!", { return }) { !categorySet.isNullOrEmpty() }
        validate(reason = "Log in to Create Hub", { return }) { userIdentifierPreferences.loggedIn }

        binding.btnNext.visibility= View.GONE
        binding.permissionProgress.visibility= View.VISIBLE

        Log.d(TAG,"category set = $categorySet")

        Log.d(TAG,"milisecond = ${System.currentTimeMillis()}")
        return request(
            _requestInProgress,
            block = {
                roomRepository.createRoom(
                    title = roomTitle.value!!,
                    time =System.currentTimeMillis(),
                    private = false,

                    categories = categorySet,
                    roomCode = CreateRoomFragment.current_room_code

                )
            },
            onSuccess = { Event.InstantRoomCreated(roomID = it).send() },
            onFailure = { Failed(it.message.orEmpty()).send() },
        )
    }

    //called from user profile room fragment to start an existing room when join Button is clicked from UserHubProfile
    fun startExistingRoom(roomName: String,roomID: String){
        return request(
            _requestInProgress,
            block = {
                roomRepository.startRoom(roomName)
            },
            onSuccess= {
                Event.ShowToast("Going Live")
                Event.RoomStarted(roomName = roomName).send()
            },
            onFailure= {
                Event.ShowToast("Going Live")
                Log.e(TAG,"Start room request network error")
            }
        )



//        return request(
//            _requestInProgress,
//            block = {
//                roomRepository.createRoom(
//                    title = roomTitle.value!!,
//                    time =System.currentTimeMillis(),
//                    private = false,
//
//                    categories = categorySet,
//                    roomCode = CreateRoomFragment.current_room_code
//
//                )
//            },
//            onSuccess = { Event.InstantRoomCreated(roomID = it).send() },
//            onFailure = { Failed(it.message.orEmpty()).send() },
//        )
    }


    private inline fun validate(
        reason: String,
        runIfConditionFalse: () -> Unit,
        condition: () -> Boolean,
    ) {
        if (!condition()) {
            Event.Failed(reason).send()
            runIfConditionFalse()
        }
    }

    fun addData(){
        if(flag) {
            viewModelScope.launch {
                try {

                    var response = registrationRepository.getVideoCategories()
                    data.value = response.data
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            flag=false
        }
    }

    sealed class Event {
        class RoomCreated(val name: String) : Event()
        class InstantRoomCreated(val roomID:String):Event()
        class Failed(val reason: String) : Event()
        class ShowToast(val message: String) : Event()

        //added to facilitate room start functionality
        class RoomStarted(val roomName: String): Event()
    }
}



abstract class BaseViewModel<T> : ViewModel() {
    private val _events = Channel<T>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    protected fun T.send() {
        viewModelScope.launch {
            _events.send(this@send)
        }
    }
}



