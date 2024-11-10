package com.searchai.api.services.explore

import com.searchai.common.models.basicModels.Info
import retrofit2.http.GET

interface ExploreInfoService {

    @GET(ExploreConstants.INFO_END_POINT)
    suspend fun getInfo(): Info
}