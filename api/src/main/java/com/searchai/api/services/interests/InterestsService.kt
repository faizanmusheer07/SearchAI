package com.searchai.api.services.interests

import com.searchai.api.constants.interests.InterestsConstants
import com.searchai.api.headers.ApiHeaders
import com.searchai.api.models.interests.response.GetCategoriesResponse
import com.searchai.api.models.interests.response.GetCategoryBranchesResponse
import com.searchai.api.models.interests.response.GetSubCategoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface InterestsService {
    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @GET(InterestsConstants.CATEGORY_ENDPOINT)
    suspend fun getCategoryBranches(): Response<List<GetCategoryBranchesResponse>>

    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @GET(InterestsConstants.CATEGORY_ENDPOINT + "/{branchId}")
    suspend fun getCategories(
        @Path("branchId") id: String = "66c05c114d6b06b6a7055282"
    ): Response<List<GetCategoriesResponse>>

    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @GET(InterestsConstants.CATEGORY_ENDPOINT + "/{branchId}/{categoryId}")
    suspend fun getSubCategories(
        @Path("branchId") branchId: String = "66c05c114d6b06b6a7055282",
        @Path("categoryId") categoryId: String
    ): Response<GetSubCategoriesResponse>
}