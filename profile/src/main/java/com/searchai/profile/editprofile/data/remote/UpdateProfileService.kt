package com.searchai.profile.editprofile.data.remote

import com.searchai.api.utils.Resource
import com.searchai.profile.editprofile.domain.model.ProfileBodySubmission
import com.searchai.profile.editprofile.domain.model.ProfileResponse
import kotlinx.coroutines.flow.Flow

interface UpdateProfileService {

    suspend fun updateProfile(profileBodySubmission: Map<String,String?>):Flow<Resource<ProfileResponse>>
}