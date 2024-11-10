package com.searchai.api.services.video


import com.searchai.api.services.video.model.body.VideoCommentBody
import cessini.technology.newapi.services.video.model.body.VideoIdBody
import com.searchai.api.services.video.model.response.ApiGetComments
import com.searchai.api.services.video.model.response.ApiLikedResult
import com.searchai.api.constants.api.params.ApiParameters
import com.searchai.api.models.ApiMessage
import com.searchai.api.services.video.model.response.ApiVideoDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface VideoService {
    @Headers(ApiParameters.NO_AUTH_HEADER)
    @GET(value = "${VideoConstants.DETAIL_ENDPOINT}{id}")
    suspend fun detail(
        @Path(value = "id") videoId: String,
    ): ApiVideoDetail

    @POST(VideoConstants.LIKE_ENDPOINT)
    suspend fun like(
        @Body videoIdBody: VideoIdBody,
    ): ApiMessage

    @Headers(ApiParameters.NO_AUTH_HEADER)
    @GET(value = "${VideoConstants.VIEW_ENDPOINT}{id}")
    suspend fun viewsAndDuration(
        @Path(value = "id") id: String,
    ): Response<ApiViewAndDurationData>

    @POST(VideoConstants.COMMENT_ENDPOINT)
    suspend fun comment(
        @Body videoCommentBody: VideoCommentBody,
    ): ApiMessage

    @Headers(ApiParameters.NO_AUTH_HEADER)
    @GET(value = "${VideoConstants.COMMENT_ENDPOINT}{id}")
    suspend fun getComment(
        @Path(value = "id") id: String,
    ): Response<ApiGetComments>

    @GET(value = "${VideoConstants.CHECK_LIKE_ENDPOINT}{id}")
    suspend fun liked(
        @Path(value = "id") id: String,
    ): Response<ApiLikedResult>

    @DELETE(value = "${VideoConstants.DELETE_ENDPOINT}{id}")
    suspend fun delete(
        @Path(value = "id") id: String,
    ): ApiMessage

    @Multipart
    @POST(VideoConstants.UPLOAD_ENDPOINT)
    suspend fun upload(
        @Part(value = "title") title: RequestBody,
        @Part(value = "duration") duration: RequestBody,
        @Part(value = "description") description: RequestBody?,
        @Part(value = "category") category: RequestBody,
        @Part thumbnail: MultipartBody.Part?,
        @Part upload_file: MultipartBody.Part,
    ): ApiMessage
}
