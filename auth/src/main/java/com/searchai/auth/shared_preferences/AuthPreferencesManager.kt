package com.searchai.auth.shared_preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.searchai.api.models.auth.response.Profile
import com.searchai.auth.constants.ProfileConstants
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthPreferencesManager @Inject constructor(
    private val context: Context
){
    private val sharedPreferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)

    private val Context.profileDataStore: DataStore<Preferences>  by preferencesDataStore(name = PROFILE_PREFERENCES)

    fun isLoggedIn(): Boolean {
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN, null)
        Log.e("Auth access key",accessToken.toString())
        return !accessToken.isNullOrEmpty()
    }

    fun storeAccessToken(accessToken: String){
        sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

    suspend fun storeProfile(profile: Profile){
        context.profileDataStore.edit { pref ->
            pref[ProfileConstants.id] = profile.id
            pref[ProfileConstants.userId] = profile.userId
            pref[ProfileConstants.name] = profile.name
            pref[ProfileConstants.email] = profile.email
            pref[ProfileConstants.areaOfExpert] = profile.areaOfExpert
            pref[ProfileConstants.profilePicture] = profile.profilePicture
            pref[ProfileConstants.channelName] = profile.channelName
            pref[ProfileConstants.bio] = profile.bio
            pref[ProfileConstants.verified] = profile.verified
            pref[ProfileConstants.provider] = profile.provider
            pref[ProfileConstants.location] = profile.location
            pref[ProfileConstants.followerCount] = profile.followerCount
            pref[ProfileConstants.followingCount] = profile.followingCount
        }
    }

    suspend fun getProfile(): Profile? {
        val preferences = context.profileDataStore.data.firstOrNull() ?: return null
        return Profile(
            id = preferences[ProfileConstants.id] ?: "",
            userId = preferences[ProfileConstants.userId] ?: "",
            name = preferences[ProfileConstants.name] ?: "",
            email = preferences[ProfileConstants.email] ?: "",
            areaOfExpert = preferences[ProfileConstants.areaOfExpert] ?: "",
            profilePicture = preferences[ProfileConstants.profilePicture] ?: "",
            channelName = preferences[ProfileConstants.channelName] ?: "",
            bio = preferences[ProfileConstants.bio] ?: "",
            verified = preferences[ProfileConstants.verified] ?: false,
            provider = preferences[ProfileConstants.provider] ?: "",
            location = preferences[ProfileConstants.location] ?: "",
            followerCount = preferences[ProfileConstants.followerCount] ?: 0,
            followingCount = preferences[ProfileConstants.followingCount] ?: 0,
            createdAt = "",
            updatedAt = "",
            webAddress = ""
        )
    }

    suspend fun deleteProfile() {
        context.profileDataStore.edit { pref ->
            pref.clear()
        }
        sharedPreferences.edit().clear().apply()
    }


    companion object{
        const val AUTH_PREFERENCES = "auth_preferences"
        const val PROFILE_PREFERENCES = "profile_preferences"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val ACCESS_TOKEN_EXPIRY = "access_token_expiry"
        const val ONE_DAY = 86400L
    }
}