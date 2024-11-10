package com.searchai.profile.editprofile.domain.service

import com.searchai.profile.editprofile.domain.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface UpdateProfileAPI {
        @PUT("api/profile")
        suspend fun updateProfile(
            @Body profile: Map<String, Any?>
        ): Response<ProfileResponse>
}