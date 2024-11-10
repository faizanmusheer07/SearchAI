package com.searchai.profile.editprofile.presentation.viewmodel

import android.app.Application
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.utils.Resource
import com.searchai.common.kotlinExtentions.checkNetwork.CheckInternet
import com.searchai.common.kotlinExtentions.constants.NetworkStatus
import com.searchai.profile.editprofile.data.remote.UpdateProfileDataStore
import com.searchai.profile.editprofile.domain.model.ProfileResponse
import com.searchai.profile.editprofile.domain.service.UpdateProfileAPI
import com.searchai.profile.followandfollower.domain.model.FollowItemModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileDataStore: UpdateProfileDataStore,
    private val checkInternet: CheckInternet,
    val app:Application
):ViewModel() {

    private val _update = MutableStateFlow<Resource<ProfileResponse>>(Resource.Loading)
    val update: Flow<Resource<ProfileResponse>> get() = _update

    fun updateProfile(profileBody:Map<String,String?>) {
        if (checkInternet.hasInternetConnection(app)){
            viewModelScope.launch {
                updateProfileDataStore.updateProfile(profileBody).collect { resource ->
                    _update.value = resource
                }
            }
        }else{
            _update.value = Resource.Error(NetworkStatus.NoInternet.toString())
        }
    }


    // Method to get current date compatible with API 24 and 26+
    fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above, use LocalDate
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
            currentDate.format(formatter)
        } else {
            // For API 24 and 25, use Calendar
            val calendar = Calendar.getInstance()
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            formatter.format(calendar.time)
        }
    }

}