package com.searchai.auth.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.searchai.api.models.auth.response.GoogleResponseBody
import com.searchai.api.models.auth.response.Profile
import com.searchai.api.utils.Resource
import com.searchai.auth.domain.repository.AuthRepository
import com.searchai.auth.shared_preferences.AuthPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authPreferencesManager: AuthPreferencesManager
) : ViewModel() {

    private val _authState: MutableLiveData<Resource<GoogleResponseBody>> = MutableLiveData(Resource.Idle)
    val authState: LiveData<Resource<GoogleResponseBody>> = _authState

    fun googleSignIn(context: Context) {
        viewModelScope.launch {
            authRepository.googleSignIn(context).collectLatest { state ->
                _authState.postValue(state)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            authPreferencesManager.deleteProfile()
        }
    }

    fun isUserLoggedIn(): Boolean {
        return authPreferencesManager.isLoggedIn()
    }

    suspend fun getCurrentUserProfile(): Profile? {
        return authPreferencesManager.getProfile()
    }

    suspend fun saveProfile(profile: Profile) {
        authPreferencesManager.storeProfile(profile)
    }

    fun storeAccessToken(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authPreferencesManager.storeAccessToken(accessToken)
        }

    }
}
