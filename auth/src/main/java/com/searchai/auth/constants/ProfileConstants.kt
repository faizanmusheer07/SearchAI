package com.searchai.auth.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object ProfileConstants {
    val id = stringPreferencesKey(name = "_id")
    val userId = stringPreferencesKey(name = "user_id")
    val name = stringPreferencesKey(name = "name")
    val email = stringPreferencesKey(name = "email")
    val areaOfExpert = stringPreferencesKey(name = "area_of_expert")
    val profilePicture = stringPreferencesKey(name = "profile_picture")
    val channelName = stringPreferencesKey(name = "channel_name")
    val bio = stringPreferencesKey(name = "bio")
    val verified = booleanPreferencesKey(name = "verifiedAt")
    val provider = stringPreferencesKey(name = "provider")
    val location = stringPreferencesKey(name = "location")
    val followerCount = intPreferencesKey(name = "follower")
    val followingCount = intPreferencesKey(name = "following")
}