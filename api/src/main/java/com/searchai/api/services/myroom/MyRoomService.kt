package com.searchai.api.services.myroom

import com.searchai.api.constants.interests.InterestsConstants
import com.searchai.api.constants.myroom.MyRoomConstants
import com.searchai.api.headers.ApiHeaders
import com.searchai.api.models.myroom.CategorySubmissionResponseModel
import com.searchai.api.models.myroom.GetMyRoomCategoriesResponse
import com.searchai.api.models.myroom.GetMyRoomSubCategoriesResponse
import com.searchai.api.models.myroom.UserRegistrationBody
import com.searchai.api.models.myroom.request.CreateRoomBody
import com.searchai.api.models.myroom.response.CreateRoomResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface MyRoomService {

    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @POST(MyRoomConstants.REGISTER_VIDEO_CATEGORY_PUBLIC_ENDPOINT)
    suspend fun registerCategory(
        @Body userRegistrationBody: UserRegistrationBody,
    ): Response<CategorySubmissionResponseModel>

    /** my room select category screen*/
//    @Headers(ApiHeaders.NO_AUTH_HEADER)
//    @GET(InterestsConstants.CATEGORY_ENDPOINT)
//    suspend fun getCategoryBranches(): Response<List<GetCategoryBranchesResponse>>

    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @GET(InterestsConstants.CATEGORY_ENDPOINT + "/{branchId}")
    suspend fun getCategories(
        @Path("branchId") id: String = "66c05c114d6b06b6a7055282"
    ): Response<List<GetMyRoomCategoriesResponse>>

    @Headers(ApiHeaders.NO_AUTH_HEADER)
    @GET(InterestsConstants.CATEGORY_ENDPOINT + "/{branchId}/{categoryId}")
    suspend fun getSubCategories(
        @Path("branchId") branchId: String = "66c05c114d6b06b6a7055282",
        @Path("categoryId") categoryId: String
    ): Response<GetMyRoomSubCategoriesResponse>


    @POST("create_room")
    suspend fun createRoom(@Body createRoomRequest: CreateRoomBody):Response<CreateRoomResponse>
}