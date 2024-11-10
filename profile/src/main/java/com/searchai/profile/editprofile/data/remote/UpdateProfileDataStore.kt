package com.searchai.profile.editprofile.data.remote

import android.util.Log
import com.searchai.api.extensions.ProvideRetrofitServices
import com.searchai.api.extensions.retrofitCallFlow
import com.searchai.api.utils.Resource
import com.searchai.profile.editprofile.domain.model.ProfileBodySubmission
import com.searchai.profile.editprofile.domain.model.ProfileResponse
import com.searchai.profile.editprofile.domain.service.UpdateProfileAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class UpdateProfileDataStore @Inject constructor(
    private val retrofitServices: ProvideRetrofitServices
) :UpdateProfileService {

    private val updateService by lazy {
        retrofitServices.createService(UpdateProfileAPI::class.java)
    }

    override suspend fun updateProfile(profileBodySubmission: Map<String,String?>):
            Flow<Resource<ProfileResponse>> = channelFlow {
        retrofitCallFlow(
            body = {updateService.updateProfile(profileBodySubmission)},
            tag = "updateProfileResponse"
        ).collectLatest { response->
            when(response){
                is Resource.Error -> {
                    send(Resource.Error(response.message))
                }
                is Resource.Loading -> {
                    send(Resource.Loading)
                }
                is Resource.Idle -> {
                    send(Resource.Idle)
                }
                is Resource.Success ->{
                    val result = response.data
                    val resultMessage = response.data?.message ?: "Unknown success response"
                    Log.d("profileUpdate", "Success: $resultMessage")
                    send(Resource.Success(result))
                }
            }
        }
    }

}